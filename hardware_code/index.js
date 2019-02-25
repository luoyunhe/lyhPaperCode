var NodeRSA = require('node-rsa')

// 定义proto接口描述文件路径
var PROTO_PATH = __dirname + '/lock.proto';
// 加载grpc模块
var grpc = require('grpc');
// 加载proto文件中helloworld包的配置
var lock_proto = grpc.load(PROTO_PATH).lock;
// 定义sayHello方法
function decryptImp(call, callback) {
    console.log(call.request)
    let ranStr = ""
    try {
        ranStr = decrypt(call.request.sign, call.request.pubKey)
    } catch (error) {
        console.log(error)
    }
    callback(null, {
        ranStr
    });
}
// 主函数
function main() {
    // 实例grpc服务
    var server = new grpc.Server();
    // 注册Greeter服务到grpc服务，并定义接口对应方法
    server.addService(lock_proto.Lock.service, { decrypt: decryptImp });
    // 绑定地址，并传入证书(这里是忽略验证)
    server.bind('0.0.0.0:50000', grpc.ServerCredentials.createInsecure());
    // 启动服务
    server.start();
};


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

function decrypt(signature, publicKey) {
    // var signature = "Z+nPmUETN7vqYXvE+onLQUbQXhDJ1FLosvDOe/YXxqGIJXyVZ7efC0Kj9wI3fGUW4mrvmRkLEggJV/Jm+0quQQ=="
    // var publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALP8qgkUz4SIjIsBPk2A1KCbvaWSPJIFoOTTZ7IHcaHYpyaJIHz+qVHE2zLmypaU9ZVBR+gkJk0kHqglzj1F1gsCAwEAAQ=="
    var pubKey = new NodeRSA(getPublicKey(publicKey))
    return pubKey.decryptPublic(signature).toString()
}
//console.log(decrypt())
main();