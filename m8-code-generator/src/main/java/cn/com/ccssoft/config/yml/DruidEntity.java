package cn.com.ccssoft.config.yml;

import java.util.ArrayList;
import java.util.List;

public class DruidEntity {

	/**
	 * 添加新的数据源以后需要在最后的方法中添加 数据源名称 不然前端获取不到
	 */
	private Source first;
	private Source second;
	private Source third;
	private Source fourth;
	private Source fifth;
	private Source sixth;
	private Source seventh;
	private Source eighth;
	
	public List<String> getSourceName(){//专门便于前端读取数据库名称的方法
		List<String> sourceList = new ArrayList<>();
		sourceList.add(first.getSourceName());
		sourceList.add(second.getSourceName());
		sourceList.add(third.getSourceName());
		sourceList.add(fourth.getSourceName());
		sourceList.add(fifth.getSourceName());
		sourceList.add(sixth.getSourceName());
		sourceList.add(seventh.getSourceName());
		sourceList.add(eighth.getSourceName());
		return sourceList;
	}

	public Source getFirst() {
		return first;
	}

	public void setFirst(Source first) {
		this.first = first;
	}

	public Source getSecond() {
		return second;
	}

	public void setSecond(Source second) {
		this.second = second;
	}

	public Source getThird() {
		return third;
	}

	public void setThird(Source third) {
		this.third = third;
	}

	public Source getFourth() {
		return fourth;
	}

	public void setFourth(Source fourth) {
		this.fourth = fourth;
	}

	public Source getFifth() {
		return fifth;
	}

	public void setFifth(Source fifth) {
		this.fifth = fifth;
	}

	public Source getSixth() {
		return sixth;
	}

	public void setSixth(Source sixth) {
		this.sixth = sixth;
	}

	public Source getSeventh() {
		return seventh;
	}

	public void setSeventh(Source seventh) {
		this.seventh = seventh;
	}

	public Source getEighth() {
		return eighth;
	}

	public void setEighth(Source eighth) {
		this.eighth = eighth;
	}
}
