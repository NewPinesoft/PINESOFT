package org.pine.aspect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.pine.annotation.ControllerLog;
import org.pine.annotation.ServiceLog;
import org.pine.annotation.ThrowExceptionLog;
import org.pine.redis.LogRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

@Component
public class LogHandler {
	@Autowired
	public LogRedisDao lrd;

	public void OprateLog(JoinPoint joinPoint, String type) {
		String ip = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			// HttpSession session = request.getSession();
			ip = request.getRemoteAddr();
		} catch (Exception ex) {
		}

		LogInfo log = new LogInfo();
		try {
			String des = "";
			if (type == "controller") {
				des = getControllerMethodDescription(joinPoint);
			} else if (type == "service") {
				des = getServiceMthodDescription(joinPoint, false);
			}
			log.setDescription(des);
			log.setMethod(
					(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			log.setRequestIp(ip);
			log.setCreateDate(new Date().toString());

			// TODO:后续处理（缓存等）
			System.out.println(JSON.toJSON(log));
			System.out.println("从redis获取：" + JSON.toJSONString(lrd.get(lrd.add(log, 7))));
		} catch (Exception ex) {
			log.setExceptionDetail("异常信息:" + ex.getMessage());
			// TODO:后续处理（缓存等）
		}
	}
	
	public void ExceptionLog(JoinPoint joinPoint, Throwable ex) {
		String ip = "";
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			ip = request.getRemoteAddr();
		} catch (Exception e) {
			// TODO: handle exception
		}

		LogInfo log = new LogInfo();
		try {
			String params = "";
			if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
				}
			}
			log.setDescription(getServiceMthodDescription(joinPoint, true));
			log.setExceptionCode(ex.getClass().getName());
			log.setType("1");
			log.setExceptionDetail(ex.getMessage());
			log.setMethod(
					(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
			log.setParams(params);
			log.setCreateBy("");
			log.setCreateDate(new Date().toString());
			log.setRequestIp(ip);

			// TODO:后续处理（缓存等）
			System.out.println(JSON.toJSON(log));
			lrd.add(log);
		} catch (Exception e) {
			log.setExceptionDetail("异常信息:" + e.getMessage());
			// TODO:后续处理（缓存等）

		}
	}

	/**
	 * 将异常信息输出到log文件
	 * 
	 * @param t
	 * @return
	 */
	public static String getTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}

	/**
	 * 获取注解中对方法的描述信息 用于service层注解
	 * 
	 * @param joinPoint
	 */
	public static String getServiceMthodDescription(JoinPoint joinPoint, Boolean isExceptionLog) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {

					if (!isExceptionLog)
						description = method.getAnnotation(ServiceLog.class).description();
					else
						description = method.getAnnotation(ThrowExceptionLog.class).description();
					break;
				}
			}
		}
		return description;
	}

	/**
	 * 获取注解中对方法的描述信息 用于Controller层注解
	 * 
	 * @param joinPoint
	 */
	public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class<?> targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == arguments.length) {
					description = method.getAnnotation(ControllerLog.class).description();
					break;
				}
			}
		}
		return description;
	}
}
