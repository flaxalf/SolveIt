from flask import Flask,jsonify
from flask import request
from flask_restful import Resource, Api
import string
import random
import time

app = Flask(__name__)
api = Api(app)


#map that contains key = match id and value = a matchInstance
matchDict = {}

class matchInstance:
    def __init__(self, id, p1 = None, p2 = None):
        self.id = id
        self.timestamp = time.time()
        self.player1 = p1
        self.player2 = p2


class matchingMulti(Resource):
    def get(self):
        global matchDict
        id = ''.join(random.choice(string.ascii_uppercase
                    + string.digits) for _ in range(8))
        user = request.args.get('user')
        match = matchInstance(id, user)
        matchDict[id] = match
        reply_msg = {'id' : id}
        return jsonify(reply_msg)

    def post(self):
        global matchDict
        id = request.args.get('id')
        user= request.args.get('user')

        if matchDict.get(id) is not None:
            if user != matchDict[id].player1 and matchDict[id].player2 is None:
                matchDict[id].player2 = user
                reply_msg = {'POST':"OK",'PLAYER1': matchDict[id].player1}
            else:
                reply_msg = {'POST':"KO"}
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
        if self.right1 == True and self.right2 == True:
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
            levelInstance.right2 = rightButton == "true"

        return jsonify("POST: ok")

api.add_resource(levelOneMulti,'/levelOneMulti')


#map that contains key = match id and value = a levelOne instance
levelTwoDict = {}

class levelTwoInstance:
    def __init__(self, id, n1 = 0, n2 = 0):
        self.id = id
        self.number1 = n1
        self.number2 = n2

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
        #check if below we need locks, cause a call from a player modifies
        #the value from the other player
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

        return jsonify("POST: ok")

api.add_resource(levelTwoMulti,'/levelTwoMulti')


class levelThreeMulti(Resource):
    def get(self):
        id = request.args.get('id')
        reply_msg = {}
        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
        return jsonify("POST: ok")

api.add_resource(levelThreeMulti,'/levelThreeMulti')


class endGameMulti(Resource):
    def get(self):
        id = request.args.get('id')
        reply_msg = {}
        return jsonify(reply_msg)

    def post(self):
        #TODO clean all the dicts here
        id = request.args.get('id')
        return jsonify("POST: ok")

api.add_resource(endGameMulti,'/endGameMulti')


class leaderboard(Resource):
    def get(self):
        reply_msg = {}
        return jsonify(reply_msg)

    def post(self):
        return jsonify("POST: ok")

api.add_resource(leaderboard,'/leaderboard')


if __name__ == '__main__':
    print('starting SolveIt apis...waiting for players')
    #app.run(host='0.0.0.0')