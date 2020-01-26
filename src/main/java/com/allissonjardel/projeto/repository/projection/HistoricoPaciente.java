package com.allissonjardel.projeto.repository.projection;

import com.allissonjardel.projeto.model.Especialidade;
import com.allissonjardel.projeto.model.Medico;
import com.allissonjardel.projeto.model.Paciente;

public interface HistoricoPaciente {

	Long getId();
	
	Paciente getPaciente();
	
	String getDataConsulta();
	
	Medico getMedico();
	
	Especialidade getEspecialidade();
}




