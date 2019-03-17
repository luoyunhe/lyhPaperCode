import Web3 from 'web3';
import fs from 'fs';
import path from 'path';
import { infuraUrl } from '../config';
// const Web3 = require('web3');
// const fs = require('fs');

const abiFilePath = path.join(__dirname, 'locker_sol_Locker.abi');
const abiStr = fs.readFileSync(abiFilePath, 'utf-8');
const abi = JSON.parse(abiStr)
const web3 = new Web3(infuraUrl);

async function getContractOwnerName(addr) {
    // if (!web3.isConnected()) {
    //     web3 = new Web3(infuraUrl);
    // }
    // console.log(web3)
    const contract = web3.eth.Contract(abi, addr);
    const owner = await contract.methods._ownerName().call();
    return owner;
}

export default getContractOwnerName;
// const res = getContractOwnerName('0x7443d2229f1ce1b545a3498abc6bb05156e1726f');
// console.log(res)