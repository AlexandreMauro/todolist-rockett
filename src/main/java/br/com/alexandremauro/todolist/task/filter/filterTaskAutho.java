package br.com.alexandremauro.todolist.task.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.alexandremauro.todolist.user.IUserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class filterTaskAutho extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {

            var authorizantion = request.getHeader("Authorization");

            var authEncoded = authorizantion.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);

            String[] Credentials = authString.split(":");

            String username = Credentials[0];
            String password = Credentials[1];

            System.out.println("Authorization");

            System.out.println(username);
            System.out.println(password);
            // petgar a autenticação(usuario e senha
            // validar usuario
            var user = this.userRepository.findByUsername(username);
            if (user == null) {

                response.sendError(401);
            } else {

                // valida senha
                var PasswordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (PasswordVerify.verified) {
                request.setAttribute("iduser",user.getId());
                    filterChain.doFilter(request, response);

                } else {
                    response.sendError(401);
                }
                // segue viagem

            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
