package com.lyh.lockersc;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
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
    private static final String BINARY = "60806040523480156200001157600080fd5b506040516200170738038062001707833981018060405260408110156200003757600080fd5b8101908080516401000000008111156200005057600080fd5b820160208101848111156200006457600080fd5b81516401000000008111828201871017156200007f57600080fd5b505092919060200180516401000000008111156200009c57600080fd5b82016020810184811115620000b057600080fd5b8151640100000000811182820187101715620000cb57600080fd5b505060008054600160a060020a031916331790558051909350620000f992506001915060208401906200011d565b5081516200010f9060029060208501906200011d565b5050600060035550620001c2565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200016057805160ff191683800117855562000190565b8280016001018555821562000190579182015b828111156200019057825182559160200191906001019062000173565b506200019e929150620001a2565b5090565b620001bf91905b808211156200019e5760008155600101620001a9565b90565b61153580620001d26000396000f3fe608060405234801561001057600080fd5b50600436106100b0576000357c010000000000000000000000000000000000000000000000000000000090048063cc4a43c011610083578063cc4a43c01461036f578063cd0dc29614610483578063d7554a6814610500578063de1561b31461063d578063f849f37914610645576100b0565b80630ffad208146100b55780633e118dbe146102065780635d8d158514610220578063b2bdfa7b1461034b575b600080fd5b6101f2600480360360608110156100cb57600080fd5b600160a060020a0382351691908101906040810160208201356401000000008111156100f657600080fd5b82018360208201111561010857600080fd5b8035906020019184600183028401116401000000008311171561012a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929594936020810193503591505064010000000081111561017d57600080fd5b82018360208201111561018f57600080fd5b803590602001918460018302840111640100000000831117156101b157600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061066b945050505050565b604080519115158252519081900360200190f35b61020e6107d2565b60408051918252519081900360200190f35b6102286107d8565b60405180806020018060200180602001848103845287818151815260200191508051906020019060200280838360005b83811015610270578181015183820152602001610258565b50505050905001848103835286818151815260200191508051906020019080838360005b838110156102ac578181015183820152602001610294565b50505050905090810190601f1680156102d95780820380516001836020036101000a031916815260200191505b50848103825285518152855160209182019187019080838360005b8381101561030c5781810151838201526020016102f4565b50505050905090810190601f1680156103395780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b610353610b1c565b60408051600160a060020a039092168252519081900360200190f35b61038c6004803603602081101561038557600080fd5b5035610b2b565b6040518084600160a060020a0316600160a060020a031681526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156103e55781810151838201526020016103cd565b50505050905090810190601f1680156104125780820380516001836020036101000a031916815260200191505b50838103825284518152845160209182019186019080838360005b8381101561044557818101518382015260200161042d565b50505050905090810190601f1680156104725780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b61048b610c82565b6040805160208082528351818301528351919283929083019185019080838360005b838110156104c55781810151838201526020016104ad565b50505050905090810190601f1680156104f25780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101f26004803603606081101561051657600080fd5b600160a060020a03823516919081019060408101602082013564010000000081111561054157600080fd5b82018360208201111561055357600080fd5b8035906020019184600183028401116401000000008311171561057557600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092959493602081019350359150506401000000008111156105c857600080fd5b8201836020820111156105da57600080fd5b803590602001918460018302840111640100000000831117156105fc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610d0f945050505050565b61048b610ed8565b6101f26004803603602081101561065b57600080fd5b5035600160a060020a0316610f30565b600080543390600160a060020a0316811461068557600080fd5b60005b6004548110156107c45760048054829081106106a057fe5b6000918252602090912060039091020154600160a060020a03878116911614156107bc5760008551111561070457846004828154811015156106de57fe5b90600052602060002090600302016001019080519060200190610702929190611307565b505b600084511115610744578360048281548110151561071e57fe5b90600052602060002090600302016002019080519060200190610742929190611307565b505b61074c611154565b6040805133815260208101829052600b818301527f6d6f646966792075736572000000000000000000000000000000000000000000606082015290517fc6ee1e1e08307ce2d854c77911cf72b51710027139dd730756959e981480edb79181900360800190a160019250506107ca565b600101610688565b50600091505b509392505050565b60035481565b606080606080606080600480549050600101604051908082528060200260200182016040528015610813578160200160208202803883390190505b50600080548251929350600160a060020a031691839190811061083257fe5b600160a060020a0390921660209283029091018201526001805460408051601f600260001961010086881615020190941693909304928301859004850281018501909152818152928301828280156108cb5780601f106108a0576101008083540402835291602001916108cb565b820191906000526020600020905b8154815290600101906020018083116108ae57829003601f168201915b505060028054604080516020601f60001961010060018716150201909416859004938401819004810282018101909252828152969950919450925084019050828280156109595780601f1061092e57610100808354040283529160200191610959565b820191906000526020600020905b81548152906001019060200180831161093c57829003601f168201915b50939550600093505050505b600454811015610b1157600480548290811061097d57fe5b60009182526020909120600390910201548251600160a060020a03909116908390600184019081106109ab57fe5b600160a060020a0390921660209283029091019091015260048054610a85918691849081106109d657fe5b90600052602060002090600302016001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a7b5780601f10610a5057610100808354040283529160200191610a7b565b820191906000526020600020905b815481529060010190602001808311610a5e57829003601f168201915b505050505061115f565b9350610b0783600483815481101515610a9a57fe5b600091825260209182902060026003909202018101805460408051601f600019610100600186161502019093169490940491820185900485028401850190528083529192909190830182828015610a7b5780601f10610a5057610100808354040283529160200191610a7b565b9250600101610965565b509591945092509050565b600054600160a060020a031681565b6004805482908110610b3957fe5b600091825260209182902060039091020180546001808301805460408051601f6002600019968516156101000296909601909316949094049182018790048702840187019052808352600160a060020a039093169550929390929190830182828015610be65780601f10610bbb57610100808354040283529160200191610be6565b820191906000526020600020905b815481529060010190602001808311610bc957829003601f168201915b50505060028085018054604080516020601f6000196101006001871615020190941695909504928301859004850281018501909152818152959695945090925090830182828015610c785780601f10610c4d57610100808354040283529160200191610c78565b820191906000526020600020905b815481529060010190602001808311610c5b57829003601f168201915b5050505050905083565b60018054604080516020600284861615610100026000190190941693909304601f81018490048402820184019092528181529291830182828015610d075780601f10610cdc57610100808354040283529160200191610d07565b820191906000526020600020905b815481529060010190602001808311610cea57829003601f168201915b505050505081565b600080543390600160a060020a03168114610d2957600080fd5b60005b600454811015610d7a576004805482908110610d4457fe5b6000918252602090912060039091020154600160a060020a0387811691161415610d725760009250506107ca565b600101610d2c565b50610d83611385565b5060408051606081018252600160a060020a0387811682526020808301888152938301879052600480546001810180835560009290925284517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b6003909202918201805473ffffffffffffffffffffffffffffffffffffffff1916919095161784559451805194959194869493610e3e937f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19c01920190611307565b5060408201518051610e5a916002840191602090910190611307565b50505050610e66611154565b60408051338152602081018290526008818301527f6164642075736572000000000000000000000000000000000000000000000000606082015290517fc6ee1e1e08307ce2d854c77911cf72b51710027139dd730756959e981480edb79181900360800190a150600195945050505050565b6002805460408051602060018416156101000260001901909316849004601f81018490048402820184019092528181529291830182828015610d075780601f10610cdc57610100808354040283529160200191610d07565b600080543390600160a060020a03168114610f4a57600080fd5b60005b600454811015611148576004805482908110610f6557fe5b6000918252602090912060039091020154600160a060020a0385811691161415611140575b6004546000190181101561105a576004805460018301908110610fa957fe5b9060005260206000209060030201600482815481101515610fc657fe5b600091825260209091208254600390920201805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039092169190911781556001808301805461102a928085019291600260001992821615610100029290920116046113b0565b506002828101805461104f9284810192916000196101006001831615020116046113b0565b505050600101610f8a565b60048054600019810190811061106c57fe5b600091825260208220600390910201805473ffffffffffffffffffffffffffffffffffffffff19168155906110a46001830182611425565b6110b2600283016000611425565b50506004805460001901906110c7908261146c565b506110d0611154565b60408051338152602081018290526008818301527f64656c2075736572000000000000000000000000000000000000000000000000606082015290517fc6ee1e1e08307ce2d854c77911cf72b51710027139dd730756959e981480edb79181900360800190a1600192505061114e565b600101610f4d565b50600091505b50919050565b600380546001019055565b6060808390506060839050606081518351016001016040519080825280601f01601f19166020018201604052801561119e576020820181803883390190505b509050806000805b855181101561121c5785818151811015156111bd57fe5b90602001015160f860020a900460f860020a0283838060010194508151811015156111e457fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053506001016111a6565b50815160018201917f0a0000000000000000000000000000000000000000000000000000000000000091849190811061125157fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535060005b84518110156112fa57848181518110151561129b57fe5b90602001015160f860020a900460f860020a0283838060010194508151811015156112c257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600101611284565b5090979650505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061134857805160ff1916838001178555611375565b82800160010185558215611375579182015b8281111561137557825182559160200191906001019061135a565b5061138192915061149d565b5090565b6060604051908101604052806000600160a060020a0316815260200160608152602001606081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106113e95780548555611375565b8280016001018555821561137557600052602060002091601f016020900482015b8281111561137557825482559160010191906001019061140a565b50805460018160011615610100020316600290046000825580601f1061144b5750611469565b601f016020900490600052602060002090810190611469919061149d565b50565b8154818355818111156114985760030281600302836000526020600020918201910161149891906114ba565b505050565b6114b791905b8082111561138157600081556001016114a3565b90565b6114b791905b8082111561138157805473ffffffffffffffffffffffffffffffffffffffff1916815560006114f26001830182611425565b611500600283016000611425565b506003016114c056fea165627a7a72305820ac2329533f6b03b66eb453e363118369db71e84932ead2446c43ece900bf31c50029";

    public static final String FUNC_MODIFYUSERINFO = "modifyUserInfo";

    public static final String FUNC__VERSION = "_version";

    public static final String FUNC_GETUSERINFO = "getUserInfo";

    public static final String FUNC__OWNER = "_owner";

    public static final String FUNC__USERS = "_users";

    public static final String FUNC__OWNERNAME = "_ownerName";

    public static final String FUNC_ADDUSER = "addUser";

    public static final String FUNC__OWNERPUBKEY = "_ownerPubKey";

    public static final String FUNC_DELUSER = "delUser";

    public static final Event UPDATE_EVENT = new Event("Update", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

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

    public RemoteCall<Tuple3<List<String>, String, String>> getUserInfo() {
        final Function function = new Function(FUNC_GETUSERINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple3<List<String>, String, String>>(
                new Callable<Tuple3<List<String>, String, String>>() {
                    @Override
                    public Tuple3<List<String>, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<String>, String, String>(
                                convertToNative((List<Address>) results.get(0).getValue()), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue());
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

    public List<UpdateEventResponse> getUpdateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPDATE_EVENT, transactionReceipt);
        ArrayList<UpdateEventResponse> responses = new ArrayList<UpdateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdateEventResponse typedResponse = new UpdateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.msg = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<UpdateEventResponse> updateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, UpdateEventResponse>() {
            @Override
            public UpdateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UPDATE_EVENT, log);
                UpdateEventResponse typedResponse = new UpdateEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.msg = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<UpdateEventResponse> updateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UPDATE_EVENT));
        return updateEventFlowable(filter);
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

    public static class UpdateEventResponse {
        public Log log;

        public String sender;

        public String msg;
    }
}
