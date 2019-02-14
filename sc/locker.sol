pragma solidity >=0.4.22 <0.6.0;
pragma experimental ABIEncoderV2;


contract Locker {
    //合约主人，即锁的拥有者
    address public _owner;
    string public _ownerName;
    string public _ownerPubKey;
    //版本号，每次合约状态变化时递增
    uint256 public _version;
    //用户结构体
    struct User {
        address addr;
        string name;
        string pubKey;
    }
    //用户列表
    User[] public _users;
    //智能锁构造函数
    constructor(string memory pubKey, string memory name) public {
        _owner = msg.sender;
        _ownerName = name;
        _ownerPubKey = pubKey;
        _version = 0;
    }
    //装饰器，用于监督是否有主人权限
    modifier onlyOwner(address sender) {
        require(sender == _owner);
        _;
    }
    //update函数
    function update() internal{
        _version++;
    }
    //添加一个新的用户
    function addUser(address addr, string memory name, string memory pubKey) public onlyOwner(msg.sender) returns (bool succ) {
        for (uint256 i = 0; i < _users.length; i++) {
            if (addr == _users[i].addr) {
                return false;
            }
        }
        User memory tmp = User(addr, name, pubKey);
        _users.push(tmp);
        update();
        return true;     
        
    }
    //修改用户信息
    function modifyUserInfo(address addr, string memory name, string memory pubKey) public onlyOwner(msg.sender) returns (bool succ) {
        for (uint256 i = 0; i < _users.length; i++) {
            if (addr == _users[i].addr) {
                if (bytes(name).length > 0) {
                    _users[i].name = name;
                }
                if (bytes(pubKey).length > 0) {
                    _users[i].pubKey = pubKey;
                }
                update();
                return true;
            }
        }
        return false;
    }
    //删除用户
    function delUser(address addr) public onlyOwner(msg.sender) returns (bool succ) {
        for (uint256 i = 0; i < _users.length; i++) {
            if (addr == _users[i].addr) {
                for(; i < _users.length - 1; i++) {
                    _users[i] = _users[i + 1];
                }
                delete _users[_users.length - 1];
                _users.length = _users.length - 1;
                update();
                return true;
            }
        }
        return false;
    }
    //返回所有用户信息
    function getUserInfo() public view returns (address, string memory, string memory, address[] memory, string[] memory, string[] memory) {
        address[] memory addrs = new address[](_users.length);
        string[] memory names = new string[](_users.length);
        string[] memory pks = new string[](_users.length);
        for (uint256 i = 0; i < _users.length; i++) {
            addrs[i] = _users[i].addr;
            names[i] = _users[i].name;
            pks[i] = _users[i].pubKey;
        }
        return (_owner, _ownerName, _ownerPubKey, addrs, names, pks);
    }
}