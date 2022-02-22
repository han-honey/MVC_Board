package vo;

/*
 * FrontController(서블릿) 에서 클라이언트로부터의 요청을 받아 
 * Action 클래스 등에서 작업을 처리 후 
 * View 페이지 또는 다른 Action 클래스로 포워딩할 때
 * 포워딩 할 URL(주소) 와 포워딩 방식(Dispatcher vs Redirect)을 다루기 위한 클래스
 * = DTO(= Bean) 역할을 수행
 */
public class ActionForward {
	private String path; // 포워딩 주소를 저장
	private boolean isRedirect; // 포워딩 방식을 저장(true : Redirect 방식, false : Dispatcher 방식)
	
	// Getter/Setter 메서드 정의
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return isRedirect;
	}
	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}
	
}















