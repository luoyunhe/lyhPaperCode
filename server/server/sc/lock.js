// import Web3 from 'web3';
// import abi from './lockAbi';
const Web3 = require('web3');
const fs = require('fs');

const abiStr = fs.readFileSync('./locker_sol_Locker.abi', 'utf-8');
const abi = JSON.parse(abiStr)

async function getContractOwner(addr) {
    const web3 = new Web3("wss://ropsten.infura.io/ws/v3/fc331766f3c8401d862db11cb785fc07");
    const contract = web3.eth.Contract(abi, addr);
    const owner = await contract.methods._ownerName().call().then(console.log);
    return owner;
}
const res = getContractOwner('0x7443d2229f1ce1b545a3498abc6bb05156e1726f');
console.log(res)