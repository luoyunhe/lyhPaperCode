1.用 solc 编译生成 .bin .abi 文件
    solcjs <Solidity文件地址>.sol --bin --abi --optimize -o <输出文件夹路径>/

2.用 web3j 生成 java 封装器
    web3j solidity generate -b /path/to/<smart-contract>.bin -a /path/to/<smart-contract>.abi -o /path/to/src/main/java -p com.your.organisation.name
    -o 后接生成好的java文件放置的位置，-p 后接生成的java文件的包名


实际使用
1.安装solc
npm install solc -g
2.编译
solcjs.cmd .\locker.sol --bin --abi --optimize -o build
3.生成java代码
..\util\web3j-4.1.1\web3j-4.1.1\bin\web3j solidity generate -b .\build\_\locker_sol_Locker.bin -a .\build\_\locker_sol_Locker.abi -o .\build\java -p com.lyh.lockersc


不支持结构体返回