# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
import grpc

import lock_pb2 as lock__pb2


class LockStub(object):
  # missing associated documentation comment in .proto file
  pass

  def __init__(self, channel):
    """Constructor.

    Args:
      channel: A grpc.Channel.
    """
    self.decrypt = channel.unary_unary(
        '/lock.Lock/decrypt',
        request_serializer=lock__pb2.Request.SerializeToString,
        response_deserializer=lock__pb2.Response.FromString,
        )


class LockServicer(object):
  # missing associated documentation comment in .proto file
  pass

  def decrypt(self, request, context):
    # missing associated documentation comment in .proto file
    pass
    context.set_code(grpc.StatusCode.UNIMPLEMENTED)
    context.set_details('Method not implemented!')
    raise NotImplementedError('Method not implemented!')


def add_LockServicer_to_server(servicer, server):
  rpc_method_handlers = {
      'decrypt': grpc.unary_unary_rpc_method_handler(
          servicer.decrypt,
          request_deserializer=lock__pb2.Request.FromString,
          response_serializer=lock__pb2.Response.SerializeToString,
      ),
  }
  generic_handler = grpc.method_handlers_generic_handler(
      'lock.Lock', rpc_method_handlers)
  server.add_generic_rpc_handlers((generic_handler,))