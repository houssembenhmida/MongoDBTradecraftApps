from flask import Flask
import pymongo
from  pymongo.collection import  ReturnDocument
from  pymongo import  MongoClient
from bson.json_util import dumps

app = Flask(__name__)


client=MongoClient("mongodb+srv://smartek:smartekpwd@smartek.2ec6p.mongodb.net/sequencedb?retryWrites=true")
db=client["sequencedb"]
collection=db["sequenceCol"]


@app.route("/")
def root():
	return("Supported method is PUT /sequence/<name>")

@app.route("/sequence/<name>", methods=['PUT'])
def sequence(name):
    #Return a constant value
    result=collection.find_one_and_update({"_id":name},{"$inc":{"counter":1}},upsert=True,return_document=ReturnDocument.AFTER)
    return dumps({ name : result["counter"]})
