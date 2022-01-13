from flask import Flask,jsonify
from flask import request
from flask_restful import Resource, Api, reqparse
import string
import random
import time

app = Flask(__name__)
api = Api(app)


parser = reqparse.RequestParser()

lat=0
lon=0


#map that contains key = match id and value = a matchInstance
matchDict = {}

class matchInstance:
    def __init__(self, id, p1 = None, p2 = None):
        self.id = id
        self.timestamp = time.time()
        self.player1 = p1
        self.player2 = p2


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

        reply_msg['success'] = levelInstance.isLevelPassed()
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

class levelTwoMulti(Resource):
    def get(self):
        id = request.args.get('id')
        reply_msg = {}
        return jsonify(reply_msg)

    def post(self):
        id = request.args.get('id')
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



class myHUB(Resource):

    def get(self):
        global lat,lon
        reply_msg = {'lat':"-1",'lon':"-1"}
        parser.remove_argument("lat")
        parser.remove_argument("lon")
        args = parser.parse_args()
        reply_msg['lat']=str(lat)
        reply_msg['lon']=str(lon)
        return jsonify(reply_msg)

    def post(self):
        global lat,lon
        parser.add_argument('lat',type=float,required=True)
        parser.add_argument('lon',type=float,required=True)
        args = parser.parse_args()
        lat=args['lat']
        lon=args['lon']
        return jsonify("POST: ok")

api.add_resource(myHUB,'/test')

if __name__ == '__main__':
    print('starting SolveIt apis...waiting for players')
    #app.run(host='0.0.0.0')