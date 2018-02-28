/*author wangjf 02/10/2018 */

package com.qiyi.hwmessenger;
import com.qiyi.hwmessenger.IHWMessengerCallback;
import com.qiyi.hwmessenger.IHWControllerClient;


interface IHWMessenger
{
	int registerCallback(IHWMessengerCallback callback);
	int unregisterCallback(IHWMessengerCallback callback);
    IHWControllerClient createHWControllerClient();
}