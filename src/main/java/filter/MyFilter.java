package filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

public class MyFilter extends ZuulFilter {

    private RequestContext ctx;
    private HttpServletRequest request;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        ctx=RequestContext.getCurrentContext();
        request=ctx.getRequest();
        String uri=request.getRequestURI();
        System.out.println(uri);
//        if ((uri != null) && (uri.equals(""))) {
//            return true;
//        }
        //return false;
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        String idToken= request.getHeader("token");
        System.out.println(idToken);
        Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(idToken).getBody();
        String id = (String) body.get("userId");
        String email=(String) body.get("emailAddress");
        String password=(String) body.get("password");
        String name=(String) body.get("name");
        System.out.println(id);
        ctx.addZuulRequestHeader("userId",id);
        ctx.addZuulRequestHeader("emailAddress",email);
        ctx.addZuulRequestHeader("password",password);
        ctx.addZuulRequestHeader("name",name);
        return ctx;
    }
}
