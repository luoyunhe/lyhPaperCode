package com.lyh.lockersc;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class Locker_sol_Locker extends Contract {
    private static final String BINARY = "60806040523480156200001157600080fd5b50604051620014e7380380620014e7833981018060405262000037919081019062000183565b60008054600160a060020a0319163317905580516200005e90600190602084019062000082565b5081516200007490600290602085019062000082565b505060006003555062000273565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620000c557805160ff1916838001178555620000f5565b82800160010185558215620000f5579182015b82811115620000f5578251825591602001919060010190620000d8565b506200010392915062000107565b5090565b6200012491905b808211156200010357600081556001016200010e565b90565b6000601f820183136200013957600080fd5b8151620001506200014a8262000218565b620001f1565b915080825260208301602083018583830111156200016d57600080fd5b6200017a83828462000240565b50505092915050565b600080604083850312156200019757600080fd5b82516001604060020a03811115620001ae57600080fd5b620001bc8582860162000127565b92505060208301516001604060020a03811115620001d957600080fd5b620001e78582860162000127565b9150509250929050565b6040518181016001604060020a03811182821017156200021057600080fd5b604052919050565b60006001604060020a038211156200022f57600080fd5b506020601f91909101601f19160190565b60005b838110156200025d57818101518382015260200162000243565b838111156200026d576000848401525b50505050565b61126480620002836000396000f3fe608060405234801561001057600080fd5b50600436106100b0576000357c010000000000000000000000000000000000000000000000000000000090048063cc4a43c011610083578063cc4a43c014610122578063cd0dc29614610144578063d7554a6814610159578063de1561b31461016c578063f849f37914610174576100b0565b80630ffad208146100b55780633e118dbe146100de5780635d8d1585146100f3578063b2bdfa7b1461010d575b600080fd5b6100c86100c3366004610e99565b610187565b6040516100d59190611139565b60405180910390f35b6100e6610288565b6040516100d59190611158565b6100fb61028e565b6040516100d596959493929190611080565b610115610687565b6040516100d5919061106c565b610135610130366004610f16565b610696565b6040516100d5939291906110fc565b61014c6107ed565b6040516100d59190611147565b6100c8610167366004610e99565b61087a565b61014c6109dd565b6100c8610182366004610e73565b610a35565b600080543390600160a060020a031681146101a157600080fd5b60005b60045481101561027a5760048054829081106101bc57fe5b6000918252602090912060039091020154600160a060020a03878116911614156102725760008551111561022057846004828154811015156101fa57fe5b9060005260206000209060030201600101908051906020019061021e929190610bfe565b505b600084511115610260578360048281548110151561023a57fe5b9060005260206000209060030201600201908051906020019061025e929190610bfe565b505b610268610bf3565b6001925050610280565b6001016101a4565b50600091505b509392505050565b60035481565b60006060806060806060806004805490506040519080825280602002602001820160405280156102c8578160200160208202803883390190505b509050606060048054905060405190808252806020026020018201604052801561030657816020015b60608152602001906001900390816102f15790505b509050606060048054905060405190808252806020026020018201604052801561034457816020015b606081526020019060019003908161032f5790505b50905060005b60045481101561053f57600480548290811061036257fe5b60009182526020909120600390910201548451600160a060020a039091169085908390811061038d57fe5b600160a060020a0390921660209283029091019091015260048054829081106103b257fe5b90600052602060002090600302016001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104575780601f1061042c57610100808354040283529160200191610457565b820191906000526020600020905b81548152906001019060200180831161043a57829003601f168201915b5050505050838281518110151561046a57fe5b60209081029091010152600480548290811061048257fe5b600091825260209182902060026003909202018101805460408051601f60001961010060018616150201909316949094049182018590048502840185019052808352919290919083018282801561051a5780601f106104ef5761010080835404028352916020019161051a565b820191906000526020600020905b8154815290600101906020018083116104fd57829003601f168201915b5050505050828281518110151561052d57fe5b6020908102909101015260010161034a565b50600054600180546040805160206002610100858716150260001901909416849004601f8101829004820283018201909352828252600160a060020a0390951694889288928892909187918301828280156105db5780601f106105b0576101008083540402835291602001916105db565b820191906000526020600020905b8154815290600101906020018083116105be57829003601f168201915b5050875460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152959a50899450925084019050828280156106695780601f1061063e57610100808354040283529160200191610669565b820191906000526020600020905b81548152906001019060200180831161064c57829003601f168201915b50505050509350985098509850985098509850505050909192939495565b600054600160a060020a031681565b60048054829081106106a457fe5b600091825260209182902060039091020180546001808301805460408051601f6002600019968516156101000296909601909316949094049182018790048702840187019052808352600160a060020a0390931695509293909291908301828280156107515780601f1061072657610100808354040283529160200191610751565b820191906000526020600020905b81548152906001019060200180831161073457829003601f168201915b50505060028085018054604080516020601f60001961010060018716150201909416959095049283018590048502810185019091528181529596959450909250908301828280156107e35780601f106107b8576101008083540402835291602001916107e3565b820191906000526020600020905b8154815290600101906020018083116107c657829003601f168201915b5050505050905083565b60018054604080516020600284861615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156108725780601f1061084757610100808354040283529160200191610872565b820191906000526020600020905b81548152906001019060200180831161085557829003601f168201915b505050505081565b600080543390600160a060020a0316811461089457600080fd5b60005b6004548110156108e55760048054829081106108af57fe5b6000918252602090912060039091020154600160a060020a03878116911614156108dd576000925050610280565b600101610897565b506108ee610c7c565b5060408051606081018252600160a060020a0387811682526020808301888152938301879052600480546001810180835560009290925284517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b6003909202918201805473ffffffffffffffffffffffffffffffffffffffff19169190951617845594518051949591948694936109a9937f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19c01920190610bfe565b50604082015180516109c5916002840191602090910190610bfe565b505050506109d1610bf3565b50600195945050505050565b6002805460408051602060018416156101000260001901909316849004601f810184900484028201840190925281815292918301828280156108725780601f1061084757610100808354040283529160200191610872565b600080543390600160a060020a03168114610a4f57600080fd5b60005b600454811015610be7576004805482908110610a6a57fe5b6000918252602090912060039091020154600160a060020a0385811691161415610bdf575b60045460001901811015610b5f576004805460018301908110610aae57fe5b9060005260206000209060030201600482815481101515610acb57fe5b600091825260209091208254600390920201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0390921691909117815560018083018054610b2f92808501929160026000199282161561010002929092011604610ca7565b5060028281018054610b54928481019291600019610100600183161502011604610ca7565b505050600101610a8f565b600480546000198101908110610b7157fe5b600091825260208220600390910201805473ffffffffffffffffffffffffffffffffffffffff1916815590610ba96001830182610d1c565b610bb7600283016000610d1c565b5050600480546000190190610bcc9082610d63565b50610bd5610bf3565b6001925050610bed565b600101610a52565b50600091505b50919050565b600380546001019055565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c3f57805160ff1916838001178555610c6c565b82800160010185558215610c6c579182015b82811115610c6c578251825591602001919060010190610c51565b50610c78929150610d94565b5090565b6060604051908101604052806000600160a060020a0316815260200160608152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610ce05780548555610c6c565b82800160010185558215610c6c57600052602060002091601f016020900482015b82811115610c6c578254825591600101919060010190610d01565b50805460018160011615610100020316600290046000825580601f10610d425750610d60565b601f016020900490600052602060002090810190610d609190610d94565b50565b815481835581811115610d8f57600302816003028360005260206000209182019101610d8f9190610db1565b505050565b610dae91905b80821115610c785760008155600101610d9a565b90565b610dae91905b80821115610c7857805473ffffffffffffffffffffffffffffffffffffffff191681556000610de96001830182610d1c565b610df7600283016000610d1c565b50600301610db7565b6000610e0c82356111c8565b9392505050565b6000601f82018313610e2457600080fd5b8135610e37610e328261118d565b611166565b91508082526020830160208301858383011115610e5357600080fd5b610e5e8382846111e4565b50505092915050565b6000610e0c8235610dae565b600060208284031215610e8557600080fd5b6000610e918484610e00565b949350505050565b600080600060608486031215610eae57600080fd5b6000610eba8686610e00565b935050602084013567ffffffffffffffff811115610ed757600080fd5b610ee386828701610e13565b925050604084013567ffffffffffffffff811115610f0057600080fd5b610f0c86828701610e13565b9150509250925092565b600060208284031215610f2857600080fd5b6000610e918484610e67565b6000610f408383610f54565b505060200190565b6000610e0c838361102b565b610f5d816111c8565b82525050565b6000610f6e826111bb565b610f7881856111bf565b9350610f83836111b5565b60005b82811015610fae57610f99868351610f34565b9550610fa4826111b5565b9150600101610f86565b5093949350505050565b6000610fc3826111bb565b610fcd81856111bf565b935083602082028501610fdf856111b5565b60005b84811015611016578383038852610ffa838351610f48565b9250611005826111b5565b602098909801979150600101610fe2565b50909695505050505050565b610f5d816111d3565b6000611036826111bb565b61104081856111bf565b93506110508185602086016111f0565b61105981611220565b9093019392505050565b610f5d81610dae565b6020810161107a8284610f54565b92915050565b60c0810161108e8289610f54565b81810360208301526110a0818861102b565b905081810360408301526110b4818761102b565b905081810360608301526110c88186610f63565b905081810360808301526110dc8185610fb8565b905081810360a08301526110f08184610fb8565b98975050505050505050565b6060810161110a8286610f54565b818103602083015261111c818561102b565b90508181036040830152611130818461102b565b95945050505050565b6020810161107a8284611022565b60208082528101610e0c818461102b565b6020810161107a8284611063565b60405181810167ffffffffffffffff8111828210171561118557600080fd5b604052919050565b600067ffffffffffffffff8211156111a457600080fd5b506020601f91909101601f19160190565b60200190565b5190565b90815260200190565b600061107a826111d8565b151590565b600160a060020a031690565b82818337506000910152565b60005b8381101561120b5781810151838201526020016111f3565b8381111561121a576000848401525b50505050565b601f01601f19169056fea265627a7a72305820c80f28dd12ceffe50ff815d88394dfd1c3406943507bb1fbc2c5a46fb5fb8e006c6578706572696d656e74616cf50037";

    public static final String FUNC_MODIFYUSERINFO = "modifyUserInfo";

    public static final String FUNC__VERSION = "_version";

    public static final String FUNC_GETUSERINFO = "getUserInfo";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC__USERS = "_users";

    public static final String FUNC__OWNERNAME = "_ownerName";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC__OWNERPUBKEY = "_ownerPubKey";

    public static final String FUNC_DELUSER = "delUser";

    @Deprecated
    protected Locker_sol_Locker(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Locker_sol_Locker(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Locker_sol_Locker(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Locker_sol_Locker(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> modifyUserInfo(String addr, String name, String pubKey) {
        final Function function = new Function(
                FUNC_MODIFYUSERINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(addr), 
                new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(pubKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> _version() {
        final Function function = new Function(FUNC__VERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple6<String, String, String, List<String>, List<String>, List<String>>> getUserInfo() {
        final Function function = new Function(FUNC_GETUSERINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteCall<Tuple6<String, String, String, List<String>, List<String>, List<String>>>(
                new Callable<Tuple6<String, String, String, List<String>, List<String>, List<String>>>() {
                    @Override
                    public Tuple6<String, String, String, List<String>, List<String>, List<String>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, List<String>, List<String>, List<String>>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                convertToNative((List<Address>) results.get(3).getValue()), 
                                convertToNative((List<Utf8String>) results.get(4).getValue()), 
                                convertToNative((List<Utf8String>) results.get(5).getValue()));
                    }
                });
    }

    public RemoteCall<String> _owner() {
        final Function function = new Function(FUNC__OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple3<String, String, String>> _users(BigInteger param0) {
        final Function function = new Function(FUNC__USERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple3<String, String, String>>(
                new Callable<Tuple3<String, String, String>>() {
                    @Override
                    public Tuple3<String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, String, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<String> _ownerName() {
        final Function function = new Function(FUNC__OWNERNAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> addUser(String addr, String name, String pubKey) {
        final Function function = new Function(
                FUNC_ADDUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(addr), 
                new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.Utf8String(pubKey)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> _ownerPubKey() {
        final Function function = new Function(FUNC__OWNERPUBKEY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> delUser(String addr) {
        final Function function = new Function(
                FUNC_DELUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(addr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Locker_sol_Locker load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Locker_sol_Locker(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Locker_sol_Locker load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Locker_sol_Locker(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Locker_sol_Locker load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Locker_sol_Locker(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Locker_sol_Locker load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Locker_sol_Locker(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Locker_sol_Locker> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String pubKey, String name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(pubKey), 
                new org.web3j.abi.datatypes.Utf8String(name)));
        return deployRemoteCall(Locker_sol_Locker.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Locker_sol_Locker> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String pubKey, String name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(pubKey), 
                new org.web3j.abi.datatypes.Utf8String(name)));
        return deployRemoteCall(Locker_sol_Locker.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Locker_sol_Locker> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String pubKey, String name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(pubKey), 
                new org.web3j.abi.datatypes.Utf8String(name)));
        return deployRemoteCall(Locker_sol_Locker.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Locker_sol_Locker> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String pubKey, String name) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(pubKey), 
                new org.web3j.abi.datatypes.Utf8String(name)));
        return deployRemoteCall(Locker_sol_Locker.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
