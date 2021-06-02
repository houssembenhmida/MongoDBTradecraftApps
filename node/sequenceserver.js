const express = require('express');
const { Db } = require('mongodb');
const MongoClient = require("mongodb").MongoClient;
const app = express()

const port = 6789
const connString= "mongodb+srv://smartek:smartekpwd@smartek.2ec6p.mongodb.net/sequencedb?retryWrites=true"
const client= new MongoClient(connString)

let db
let collection


client.connect(function(err){
    if(err==null){
         db=client.db("sequencedb")
         collection=db.collection("sequenceCol")
    //    client.close
    }
})


function sequence(req,res) {
    name = req.params['name']
    
    collection.findOneAndUpdate({"_id":name},{"$inc":{"counter":1}},{upsert:true,returnOriginal:false},function(err,result){
        if(err==null){
            let response={}
            // counter=result["value"]
            counter=result.value["counter"]
            response[name]=counter
            res.send(response)
            // return result

        }
    })
    
   
    
    //  let doc= await collection.fi
 
}

app.put('/sequence/:name', sequence)

app.listen(port, () => console.log(`Example app listening on port ${port}!`))



        