from flask import Flask,jsonify
from flask import request
from flask_restful import Resource, Api
import string
import random
import time
from time import gmtime
from time import strftime

from threading import Lock

import mysql.connector


def connectToDB():
    return mysql.connector.connect(
              host="CondivisoMobile.mysql.pythonanywhere-services.com",
              user="CondivisoMobile",
              password="DatabasePWD",
              database="CondivisoMobile$default"
            )



app = Flask(__name__)
api = Api(app)


#map that contains key = match id and value = a matchInstance
matchDict = {}

class matchInstance:
    def __init__(self, id, p1 = None, p2 = None):
        self.id = id
        self.player1 = p1
        self.player2 = p2
        self.lock = Lock()
        self.timestamp = None
        self.resultRegistered = False
        self.finishTime = None


class matchingMulti(Resource):
    def get(self):
        id = request.args.get('id')

        reply_msg = {}
        if id is None:
            #first call of the host request
            id = ''.join(random.choice(string.ascii_uppercase
                        + string.digits) for _ in range(8))
            user = request.args.get('user')
            match = matchInstance(id, user)
            matchDict[id] = match
            reply_msg['id'] = id
        else:
            #the player that is hosting is waiting for the second player
            if matchDict.get(id) is None:
                reply_msg['matching'] = 'error'
            else:
                matchingInstance = matchDict[id]
                if matchingInstance.player2 is not None:
                    matchingInstance.timestamp = time.time()
                    reply_msg['matching'] = 'start'
                    reply_msg['PLAYER2'] = matchingInstance.player2

                else:
                    reply_msg['matching'] = 'wait'

        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
        user= request.args.get('user')

        if matchDict.get(id) is not None:
            matchingInstance = matchDict[id]
            matchingInstance.lock.acquire()
            if user != matchingInstance.player1 and matchingInstance.player2 is None:
                matchingInstance.player2 = user
                reply_msg = {'POST':"OK",'PLAYER1': matchingInstance.player1}
            else:
                reply_msg = {'POST':"KO"}
            matchingInstance.lock.release()
        else:
            reply_msg = {'POST':"KO"}

        return jsonify(reply_msg)

api.add_resource(matchingMulti,'/matchingMulti')



#map that contains key = match id and value = a levelOne instance
levelOneDict = {}

class levelOneInstance:
    def __init__(self, id, b1 = 0, b2 = 0):
        self.id = id
        self.button1 = b1
        self.button2 = b2
        self.right1 = False
        self.right2 = False

    def isLevelPassed(self):
        if self.right1 and self.right2:
            return True
        return False


class levelOneMulti(Resource):
    def get(self):
        id = request.args.get('id')
        user = request.args.get('user')

        reply_msg = {}
        if levelOneDict.get(id) is None:
            levelOneDict[id] = levelOneInstance(id)

        levelInstance = levelOneDict[id]
        if user == matchDict[id].player1:
            reply_msg['button'] = levelInstance.button2
        else:
            reply_msg['button'] = levelInstance.button1

        passed = levelInstance.isLevelPassed()
        reply_msg['success'] = passed

        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
        user = request.args.get('user')
        buttonPressed = request.args.get('button')
        rightButton = request.args.get('right')

        if levelOneDict.get(id) is None:
            levelOneDict[id] = levelOneInstance(id)

        levelInstance = levelOneDict[id]
        if user == matchDict[id].player1:
            levelInstance.button1 = buttonPressed
            levelInstance.right1 = rightButton.lower() == "true"
        else:
            levelInstance.button2 = buttonPressed
            levelInstance.right2 = rightButton.lower() == "true"

        return jsonify("POST: OK")

api.add_resource(levelOneMulti,'/levelOneMulti')


#map that contains key = match id and value = a levelThree instance
levelTwoDict = {}

class levelTwoInstance:
    def __init__(self, id, n1 = 0, n2 = 0):
        self.id = id
        self.number1 = n1
        self.number2 = n2
        self.lock = Lock()

    def isLevelPassed(self):
        if self.number1 == self.number2:
            return True
        return False



class levelTwoMulti(Resource):
    def get(self):
        id = request.args.get('id')
        user = request.args.get('user')

        reply_msg = {}
        if levelTwoDict.get(id) is None:
            levelTwoDict[id] = levelTwoInstance(id)

        levelInstance = levelTwoDict[id]
        if levelInstance.number1 != 0 and levelInstance.number2 != 0:
            if user == matchDict[id].player1:
                reply_msg['number'] = levelInstance.number2
            else:
                reply_msg['number'] = levelInstance.number1

            passed = levelInstance.isLevelPassed()
            reply_msg['success'] = passed

        else:
            reply_msg['success'] = 'wait'

        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
        user = request.args.get('user')
        number = request.args.get('number')

        if levelTwoDict.get(id) is None:
            levelTwoDict[id] = levelTwoInstance(id)

        levelInstance = levelTwoDict[id]
        #lock, so if a reset is needed, it will happen just one time, even if
        #both users try to post at the same time
        levelInstance.lock.acquire()
        if user == matchDict[id].player1:
            if levelInstance.number1 != 0:
                #clean the level instance here, if someone posts a new number
                #after already posting one, it means that the level has
                #restarted so the status of the instance must be restarted,
                #so the player number must be set to the new one
                #and the other player number must be set to 0 again
                levelInstance.number2 = 0
            levelInstance.number1 = number
        else:
            if levelInstance.number2 != 0:
                #same as above
                levelInstance.number1 = 0
            levelInstance.number2 = number

        levelInstance.lock.release()
        return jsonify("POST: OK")

api.add_resource(levelTwoMulti,'/levelTwoMulti')


#map that contains key = match id and value = a levelThree instance
levelThreeDict = {}

class levelThreeInstance:
    def __init__(self, id, c = 0, g = 10):
        self.id = id
        self.count = c
        self.goal = g
        self.lock = Lock()

    def increaseCountByOne(self):
        self.lock.acquire()

        self.count += 1
        if self.count > self.goal:
            self.count = 0

        self.lock.release()

    def readCount(self):
        self.lock.acquire()
        val = self.count
        self.lock.release()
        return val


class levelThreeMulti(Resource):
    def get(self):
        id = request.args.get('id')
        if levelThreeDict.get(id) is None:
            levelThreeDict[id] = levelThreeInstance(id)

        levelInstance = levelThreeDict[id]
        reply_msg = {'count' : levelInstance.readCount()}

        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
        if levelThreeDict.get(id) is None:
            levelThreeDict[id] = levelThreeInstance(id)

        levelInstance = levelThreeDict[id]
        levelInstance.increaseCountByOne()
        reply_msg = {'POST' : 'OK', 'count' : levelInstance.readCount()}

        return jsonify(reply_msg)

api.add_resource(levelThreeMulti,'/levelThreeMulti')


class endGameMulti(Resource):
    def get(self):
        #this get will not be used
        reply_msg = {}
        return jsonify(reply_msg)

    def post(self):
        now = time.time()
        id = request.args.get('id')
        if matchDict.get(id) is None:
            return jsonify("POST: KO")

        matchingInstance = matchDict[id]
        reply_msg = {}
        matchingInstance.lock.acquire()
        if not matchingInstance.resultRegistered:
            squad = matchingInstance.player1 + ' & ' + matchingInstance.player2
            timeDiff = round(now - matchingInstance.timestamp)
            if timeDiff >= 3600: #db supports max 5 chars (mm:ss)
                return jsonify("POST: KO")
            timeFormatted = strftime("%M:%S", gmtime(timeDiff))

            connection = connectToDB()
            cursor = connection.cursor()
            sql = "INSERT INTO leaderboard (squad, time) VALUES (%s, %s)"
            val = (squad, timeFormatted)
            cursor.execute(sql, val)

            connection.commit()
            cursor.close()
            connection.close()
            matchingInstance.resultRegistered = True
            matchingInstance.finishTime = timeFormatted
            reply_msg["time"] = timeFormatted
        else:
            reply_msg["time"] = matchingInstance.finishTime
            matchDict.pop(id, None)
            levelOneDict.pop(id, None)
            levelTwoDict.pop(id, None)
            levelThreeDict.pop(id, None)

        matchingInstance.lock.release()
        reply_msg['POST'] = "OK"
        return jsonify(reply_msg)

api.add_resource(endGameMulti,'/endGameMulti')


class leaderboard(Resource):
    def get(self):
        connection = connectToDB()

        cursor = connection.cursor()
        cursor.execute("SELECT * FROM leaderboard ORDER BY time ASC")
        result = cursor.fetchall()

        reply_msg = {}
        count = 1
        for x in result:
            reply_msg[count] = [x[0], x[1]]
            count += 1

        cursor.close()
        connection.close()
        return jsonify(reply_msg)

    def post(self):
        #this post will not be used
        return jsonify("POST: OK")

api.add_resource(leaderboard,'/leaderboard')


if __name__ == '__main__':
    print('starting SolveIt apis...waiting for players')
    #app.run(host='0.0.0.0')