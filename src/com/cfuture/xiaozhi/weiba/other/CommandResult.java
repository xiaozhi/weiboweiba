package com.cfuture.xiaozhi.weiba.other;

/**
 * Process 结果的一个封装类
 * @author Thunder
 *
 */
public class CommandResult {
	
	private Integer result;      // 执行后代码
	private String resultMsg;    // 输出信息
	private String resultErrMsg; // 错误信息
	
	public CommandResult() {
		super();
	}

	public CommandResult(Integer result, String resultMsg, String resultErrMsg) {
		super();
		this.result = result;
		this.resultMsg = resultMsg;
		this.resultErrMsg = resultErrMsg;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getResultErrMsg() {
		return resultErrMsg;
	}

	public void setResultErrMsg(String resultErrMsg) {
		this.resultErrMsg = resultErrMsg;
	}

	@Override
	public String toString() {
		return "CommandResult [result=" + result + ", resultMsg=" + resultMsg
				+ ", resultErrMsg=" + resultErrMsg + "]";
	}
	
	public boolean success()
    {
        return this.result == 0;
    }
}
