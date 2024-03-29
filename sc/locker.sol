pragma solidity >=0.4.22 <0.6.0;

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

    //定义更新事件
    event Update(address sender, string msg);

    //智能合约构造函数
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
        emit Update(msg.sender, "add user");
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
                emit Update(msg.sender, "modify user");
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
                emit Update(msg.sender, "del user");
                return true;
            }
        }
        return false;
    }
    //返回所有用户信息
    function getUserInfo() public view returns (address[] memory, string memory, string memory) {

        string memory names;
        string memory pks;
        address[] memory addrs = new address[](_users.length + 1);
        addrs[0] = _owner;
        names = _ownerName;
        pks = _ownerPubKey;
        for (uint256 i = 0; i < _users.length; i++) {
            addrs[i + 1] = _users[i].addr;
            names = strConcat(names, _users[i].name);
            pks = strConcat(pks, _users[i].pubKey);
        }
        return (addrs, names, pks);
    }
    function strConcat(string memory _a, string memory _b) internal pure returns (string memory){
        bytes memory _ba = bytes(_a);
        bytes memory _bb = bytes(_b);
        string memory ret = new string(_ba.length + _bb.length + 1);
        bytes memory bret = bytes(ret);
        uint k = 0;
        for (uint i = 0; i < _ba.length; i++) {
            bret[k++] = _ba[i];
        }
        bret[k++] = '\n';
        for (uint i = 0; i < _bb.length; i++) {
            bret[k++] = _bb[i];
        }
        return string(bret);
   }  


}   