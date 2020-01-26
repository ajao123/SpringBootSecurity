package com.allissonjardel.projeto;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSecurityApplication{

	public static void main(String[] args) {
		
		//System.out.println(new BCryptPasswordEncoder().encode("123456"));
	
		
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
		SpringApplication.run(DemoSecurityApplication.class, args);
	}

//	@Autowired
//	JavaMailSender sender;
//	
//	@Autowired
//	EmailServiceImpl service;
//	
//	@Override
//	public void run(String... args) throws Exception {
//		
//		service.enviarPedidoDeConfirmacaoDeCadastro("allissonjardelec@gmail.com", "3453dsa");
//		
//		SimpleMailMessage simple = new SimpleMailMessage();
//		simple.setTo("allissonjardelec@gmail.com");
//		simple.setText("Teste numero 1");
//		simple.setSubject("teste 1");
//		sender.send(simple);
//	}
}
