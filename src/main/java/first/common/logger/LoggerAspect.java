package first.common.logger;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggerAspect {
    protected Log log = LogFactory.getLog(LoggerAspect.class);
    static String name = "";
    static String type = "";
     
    /** 
     * around advice : 조인포인트의 전 과정에 수행되는 advice
     * execution :포인트컷 표현식,가장 대표적이고 강력한 지시자로, 
     * 		접근제어자, 리턴 타입, 타입 패턴, 메서드, 파라미터 타입,예외 타입 등을 조합해서 메서드까지 선택가능한 가장 정교한 포인트컷을 만들수 있다.
     */
    @Around("execution(* first..controller.*Controller.*(..)) or execution(* first..service.*Impl.*(..)) or execution(* first..dao.*DAO.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        type = joinPoint.getSignature().getDeclaringTypeName();
         
        if (type.indexOf("Controller") > -1) {
            name = "Controller  \t:  ";
        }
        else if (type.indexOf("Service") > -1) {
            name = "ServiceImpl  \t:  ";
        }
        else if (type.indexOf("DAO") > -1) {
            name = "DAO  \t\t:  ";
        }
        log.debug(name + type + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}