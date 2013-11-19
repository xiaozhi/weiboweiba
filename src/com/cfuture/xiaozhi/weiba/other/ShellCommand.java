package com.cfuture.xiaozhi.weiba.other;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import android.util.Log;

/**
 * ShellCommand的一个封装
 * 
 * @author Thunder
 * 
 */
public class ShellCommand {

	// ShellCommand 对象
	public static ShellCommand shellCommand;

	// Shell 对象, 角色设计模式
	public Shell su;

	// 角色
	public final static String USER_MODEL_SUPERUSER = "su";

	/**************** 命令常量 ********************/
	public final static String MOUNT_SYSTEM_RW = "mount -o remount,rw /dev/block/mtdblock3 /system";
	public final static String CHMOD_0777 = "chmod 0777 ";
	public final static String CHMOD_0644 = "chmod 0644 ";
	public final static String CHMOD_0600 = "chmod 0600 ";
	public final static String CHOWN = "chown ";
	public final static String CP = "cp ";

	/**
	 * 构造函数
	 */
	public ShellCommand() {
		this.su = new Shell(USER_MODEL_SUPERUSER);
	}

	/**
	 * 判断是否可以Root
	 * 
	 * @param paramBoolean
	 * @return
	 */
	public boolean canSU() {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream dataOutputStream = new DataOutputStream(
					process.getOutputStream());
			dataOutputStream
					.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");
			dataOutputStream.close();

			process.waitFor();
			if (process.exitValue() != 255) {
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 角色 SuperUser， 封装成对象
	 * 
	 * @author Thunder
	 * 
	 */
	public class Shell {

		private String SHELL = "su";
		protected boolean setLibPath = true;

		/**
		 * 构造函数
		 * 
		 * @param model
		 *            ， 指定角色
		 */
		public Shell(String model) {
			this.SHELL = model;
		}

		/**
		 * 对Shell结果的一个读取
		 * 
		 * @param inputStream
		 * @return
		 */
		public String getStreamLines(InputStream inputStream) {
			DataInputStream dataInputStream = new DataInputStream(inputStream);
			StringBuffer stringBuffer = new StringBuffer();

			try {
				if (dataInputStream.available() > 0) {
					stringBuffer = new StringBuffer(dataInputStream.readLine());
				}
			} catch (Exception localException2) {
				try {
					if (dataInputStream.available() <= 0) {
						return stringBuffer.toString();
					}
				} catch (Exception e) {
					Log.e("getStreamLines Error", e.getMessage());
				}
			}
			return null;
		}

		/**
		 * 执行Shell语句
		 * 
		 * @param exeString
		 *            , 指定执行的语句
		 * @return
		 */
		public Process run(String exeString) {
			Runtime runtime = Runtime.getRuntime();
			Process process = null;

			try {
				process = runtime.exec(this.SHELL); // SuperUser Model 下

				DataOutputStream dataOutputStream = new DataOutputStream(
						process.getOutputStream());

				dataOutputStream.writeBytes("exec " + exeString + "\n"); // 执行Shell
				dataOutputStream.flush();
				dataOutputStream.close();

			} catch (Exception e) {
				Log.e("run Error", e.getMessage());
			}

			return process;
		}

		/**
		 * 对Shell结果集的一个封装
		 * 
		 * @param executeString
		 * @return
		 * @throws InterruptedException
		 */
		public CommandResult runWaitFor(String executeString)
				throws InterruptedException {

			Process process = run(executeString);
			CommandResult commandResult = new CommandResult();
			commandResult.setResult(Integer.valueOf(process.waitFor()));
			// 打印执行结果 0 为正常结束
			Log.e("result", commandResult.getResult() + "");
			commandResult
					.setResultMsg(getStreamLines(process.getInputStream()));
			commandResult.setResultErrMsg(getStreamLines(process
					.getErrorStream()));

			return commandResult;
		}

		/**
		 * 在 BusyBox 的前提下去运行Shell
		 * 
		 * @param executeString
		 * @return
		 * @throws InterruptedException
		 */
		public CommandResult runBusyBox(String executeString)
				throws InterruptedException {
			return runWaitFor("busybox " + executeString);
		}
	}
}