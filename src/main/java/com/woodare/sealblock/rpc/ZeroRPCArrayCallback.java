//
// MessagePack-RPC for Java
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package com.woodare.sealblock.rpc;

import java.util.List;

/**
 * Callback interface for RPC methods. Use this interface to mark a method
 * exposed on a RPC handler as asynchronous. If the methods first parameter is
 * assignable from {@link ZeroRPCArrayCallback} the server won't explicitly wait for the
 * methods execution, instead the executed method is responsible for triggering
 * a response by calling one of the callbacks methods.
 * <p>
 * Example:
 * 
 * <pre>
 *   class RPCServer {
 *     public void doAsync(Callback<String> callback, String action) {
 *       // do some action
 *       if (success) 
 *         callback.run(actionResult);
 *       else
 *         callback.onError(null, "failed"):
 *     }
 * </pre>
 */
public interface ZeroRPCArrayCallback<T> {

	Class<T> getObjClass();

	/**
	 * Passes the methods result back to the caller.
	 * 
	 * @param result
	 *            the methods result
	 */
	public void onResult(List<T> result);

	/**
	 * Passes the result and an object representing the error back to the caller
	 * in the case of an exception.
	 * 
	 * @param error
	 * @param e
	 * @param result
	 */
	public void onError(Object error, Throwable e, List<T> result);
}
