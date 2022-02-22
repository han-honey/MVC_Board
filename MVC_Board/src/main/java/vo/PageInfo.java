package vo;

// 페이징 처리 관련 정보를 저장하는 PageInfo 클래스
public class PageInfo {
	private int pageNum; // 현재 페이지 번호
	private int maxPage; // 최대 페이지 번호
	private int startPage; // 시작 페이지 번호
	private int endPage; // 끝 페이지 번호
	private int listCount; // 총 게시물 수
	
	public PageInfo() {}

	public PageInfo(int pageNum, int maxPage, int startPage, int endPage, int listCount) {
		super();
		this.pageNum = pageNum;
		this.maxPage = maxPage;
		this.startPage = startPage;
		this.endPage = endPage;
		this.listCount = listCount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public int getListCount() {
		return listCount;
	}

	public void setListCount(int listCount) {
		this.listCount = listCount;
	}
	
}











