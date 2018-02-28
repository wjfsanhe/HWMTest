/*author wangjf 02/10/2018 */

package com.qiyi.hwmessenger;

interface IHWMessengerCallback
{
	void onKey(String deviceName, int keyCode, int value, int flags);
}
