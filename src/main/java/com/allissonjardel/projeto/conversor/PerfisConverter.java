package com.allissonjardel.projeto.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.allissonjardel.projeto.model.Perfil;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>>{

	@Override
	public List<Perfil> convert(String[] source) {
		List<Perfil> perfis= new ArrayList<>();
		for(String id : source) {
			// PS: Avaliar a página html, essa solução foi criada pq caso so tenho uma opção de perfil
			// O html so retorna 1 perfil, e não um list com 1 perfil, então não entraria nesse conversor
			// e seria necessário criar outro conversor, no entando adicionamos um valor antes de adicionar-
			// mos o perfil, assim haverá sempre pelo menos 2 valores.
			
			if(!id.equals("0")) {
				perfis.add(new Perfil(Long.parseLong(id)));
			}
		}
		return perfis;
	}

}
