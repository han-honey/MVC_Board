package vo;

import java.sql.Date;

/*
CREATE TABLE file_board (
	num INT PRIMARY KEY,
	name VARCHAR(16) NOT NULL,
	pass VARCHAR(16) NOT NULL,
	subject VARCHAR(100) NOT NULL,
	content VARCHAR(1000) NOT NULL,
	file VARCHAR(100) NOT NULL,
	original_file VARCHAR(100) NOT NULL,
	re_ref INT NOT NULL,
	re_lev INT NOT NULL,
	re_seq INT NOT NULL,
	date DATETIME NOT NULL,	
	readcount INT NOT NULL
 );
*/
public class FileBoardDTO {
	private int num;
	private String name;
	private String pass;
	private String subject;
	private String content;
	private String file;
	private String original_file;
	private int re_ref; // 참조글번호
	private int re_lev; // 들여쓰기레벨
	private int re_seq; // 답글순서번호
	private Date date;
	private int readcount;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getOriginal_file() {
		return original_file;
	}
	public void setOriginal_file(String original_file) {
		this.original_file = original_file;
	}
	public int getRe_ref() {
		return re_ref;
	}
	public void setRe_ref(int re_ref) {
		this.re_ref = re_ref;
	}
	public int getRe_lev() {
		return re_lev;
	}
	public void setRe_lev(int re_lev) {
		this.re_lev = re_lev;
	}
	public int getRe_seq() {
		return re_seq;
	}
	public void setRe_seq(int re_seq) {
		this.re_seq = re_seq;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	
	@Override
	public String toString() {
		return "FileBoardDTO [num=" + num + ", name=" + name + ", pass=" + pass + ", subject=" + subject + ", content="
				+ content + ", file=" + file + ", original_file=" + original_file + ", re_ref=" + re_ref + ", re_lev="
				+ re_lev + ", re_seq=" + re_seq + ", date=" + date + ", readcount=" + readcount + "]";
	}
	
}







