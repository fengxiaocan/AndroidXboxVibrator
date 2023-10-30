import android.content.Context
import com.ayaneo.gamewindow.utils.usbManager

fun Context.xbox360Vibrator() {
  val usbManager = usbManager()
  usbManager.deviceList.forEach { entry ->
    val usbDevice = entry.value
    val deviceConnection = usbManager.openDevice(usbDevice)
    deviceConnection?.let { connection ->
      for (i in 0 until usbDevice.interfaceCount) {
        val usbInterface = usbDevice.getInterface(i)
        if (connection.claimInterface(usbInterface, true)) {
          //check permission
          for (i1 in 0 until usbInterface.endpointCount) {
            //不确定是哪一个 usbEndpoint，就全部发送数据包，或者制定某个
            val usbEndpoint = usbInterface.getEndpoint(i1)
            val bytes = byteArrayOf(0x0, 0x08, 0x0, 0xff.toByte(), 0xff.toByte(), 0x0, 0x0, 0x0)
            connection.bulkTransfer(usbEndpoint, bytes, bytes.size, 1000)
          }
          connection.close()
        } else {
          //无通信权限
          connection.close()
        }
      }
    }
  }
}
