var NodeRSA = require('node-rsa')
function insert_str(str, insert_str, sn) {
    var newstr = "";
    for (var i = 0; i < str.length; i += sn) {
        var tmp = str.substring(i, i + sn);
        newstr += tmp + insert_str;
    }
    return newstr;
}
function getPublicKey(key) {
    var result = insert_str(key, '\n', 64);
    return '-----BEGIN PUBLIC KEY-----\n' + result + '-----END PUBLIC KEY-----';
};

function decrypt() {
    // var signature = "mg77dJmenUhK4sU24MpCZxKacnduGgPvB7JD7xJTLGqKJHCTybkieyYYvRjdcCnIDIvEwuMwHNvhRI9k8+b35w=="
    // var publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALs4LNpZT4jBVPHm9mCTZAtZ20Jmli8DoDgIFSpoDpa1ptyR9FE9IO+rcZzBHXxEe2oQIktj5lCt7pEIO0/DZ4kCAwEAAQ=="
    var signature = "bQwFBapRMuPRIysbyhZh0r5zdkxWN0Fca2Rqw1NLkP15U0/UmhVvfPDlrkGd+hy0tu7DoIGC3J7VkLFgezEQIg==";
    var publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJhS1xC7z+ZS+jWKekZ16kPBJv7mN1bifev/YrkWOkP/XKubJ9MWtpPiNoRpIWLXPQWd+X6qbYXWiHafCDSaxEsCAwEAAQ=="
    var pubKey = new NodeRSA(getPublicKey(publicKey))
    return pubKey.decryptPublic(signature).toString()
}
console.log(decrypt())