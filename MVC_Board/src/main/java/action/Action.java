package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vo.ActionForward;

/*
 * FrontController 로 요청이 전달되면 요청에 해당하는 Action 클래스로 이동해야하는데
 * 이 때, Action 클래스의 execute() 메서드를 호출하여 작업을 진행하고
 * 작업 결과를 공통 항목인 ActionForward 객체 형태로 리턴받아야 함
 * => 위의 과정을 여러 Action 클래스에서 동일한 형태로 수행해야하므로 코드의 통일성 필요
 * => 따라서, 각 Action 클래스에서 수행할 메서드를 하나의 추상메서드로 제공하기 위해
 *    Action 인터페이스를 설계하고, 공통 항목인 execute() 메서드를 정의하여
 *    각 Action 클래스에서 Action 인터페이스를 상속받아 execute() 메서드를 구현하면
 *    코드의 통일성이 향상되고, 차후에 다형성 활용도 가능해짐 
 */
public interface Action {
	// 각 요청을 받아들일 execute() 메서드를 추상메서드로 정의하고
	// => 파라미터로 HttpServletRequest 객체와 HttpServletResponse 객체를 전달받음
	// => 포워딩 정보(URL 과 포워딩 방식)를 갖는 ActionForward 타입 객체를 리턴함
	// => 내부에서 발생하는 모든 예외를 외부로 위임
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
















