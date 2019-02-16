const NodeRSA = require('node-rsa')

var signature = "bQwFBapRMuPRIysbyhZh0r5zdkxWN0Fca2Rqw1NLkP15U0/UmhVvfPDlrkGd+hy0tu7DoIGC3J7VkLFgezEQIg==";
var publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJhS1xC7z+ZS+jWKekZ16kPBJv7mN1bifev/YrkWOkP/XKubJ9MWtpPiNoRpIWLXPQWd+X6qbYXWiHafCDSaxEsCAwEAAQ=="




function insert_str(str, insert_str, sn) {
    var newstr = "";
    for (var i = 0; i < str.length; i += sn) {
        var tmp = str.substring(i, i + sn);
        newstr += tmp + insert_str;
    }
    return newstr;
}
var getPublicKey = function(key){
    var result = insert_str(key, '\n', 64);
    return '-----BEGIN PUBLIC KEY-----\n' + result + '-----END PUBLIC KEY-----';
};

var pubKey = new NodeRSA(getPublicKey(publicKey))
console.log(pubKey.decryptPublic(signature).toString())
