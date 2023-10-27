/ *  VARIÁVEIS GLOBAIS */

var tblDePara = "EMPRESAS_X_TEMPLATES_ia_pt"; // Nome da tabela DE/PARA de Empresa/Segmento X Template
var tptMain = "ZW_RESULTADO_OPERACIONAL_MAIN"; // Nome do redirecionador (template atual)
var tptPrefixo = "ZW_RESULTADOS_OPERACIONAIS_"; // Prefixo padrão dos nomes dos Templates
var varEmpresa =  "ZC_M_M0COMP_CODE"; // Nome da variável que identifica a Empresa
var varSegmento =  "ZC_V_MZSEGMENT"; // Nome da variável que identifica o Segmento
var dpFiltroInicial = "DP_FILTRO_INICIAL" // Nome do data provider que contém os filtros iniciais

/* Escondendo a tabela HTML com o DE/PARA de Empresa/Segmento X Template */
document.getElementById(tblDePara).style.visibility = "hidden";

/* Função que lê um XML para verificar qual a empresa e o segmento selecionados, e direciona o usuário para o template correspondente */
function redirectTemplate()
{
	// Escondendo a tabela HTML com o DE/PARA de Empresa/Segmento X Template
	document.getElementById(tblDePara).style.visibility = "hidden";

	// Lendo a página por tag
	root = document.childNodes[0]; // <HTML>
	body = root.getElementsByTagName("BODY")[0]; // <BODY>
                xml = body.getElementsByTagName("XML")[0]; // <XML>
	bics = xml.childNodes[0]; // <BICS_VIEW>

	// Selecionando as variáveis do XML
	vars = bics.getElementsByTagName("VARIABLES")[0]; // <VARIABLES>
	varl = vars.getElementsByTagName("VARIABLE"); // Número de variáveis

	// Flags para Empresa e Segmento
	achouEmpresa = false;
	achouSegmento = false;

	// Percorrendo as variáveis
	for ( i = 0; i <= varl.length; i++ )
	{
		vari = vars.getElementsByTagName("VARIABLE")[i];

		// Nome da variável
		varnam = vari.attributes[1].text;

		// Verificando se a variável lida no XML é a variável Empresa
		if (varnam == varEmpresa)
		{
			// Valor da variável
			mem = vari.getElementsByTagName("MEMBER")[0];
			compCode = mem.attributes.getNamedItem("name").value;

			achouEmpresa = true;
		}
		// Verificando se a variável lida no XML é a variável Segmento
		else if (varnam == varSegmento)
		{
			// Valor da variável
			mem = vari.getElementsByTagName("MEMBER")[0];
			segmento = mem.attributes.getNamedItem("name").value;

			achouSegmento = true;
		}

		if (achouEmpresa && achouSegmento)
		{
			break;
		}
	}
	if (compCode != null && segmento != null)
	{
	// Chamando função que identifica o template por empresa e segmento
	template = findTemplate(compCode, segmento);
		if (template != "" && template != null)
		{
			// Executando função padrão que transfere o usuário para o template correto
			executeJS_TRANSFER_STATE_R(template);

		} else {

			alert("Template não encontrado para a empresa " + compCode + " e o segmento " + segmento +"!");

			// Executando função que recarrega o template atual
			executeJS_SET_TEMPLATE_R();
		}
	}
}

/* Função que percorre uma tabela HTML buscando o template corresponde a uma empresa e um segmento */
function findTemplate(givenCompCode, givenSegmento)
{
	var template = "";

	// Capturando tabela HTML
	var tbl = document.getElementById(tblDePara);

	// Número de linhas da tabela
	var tblLength = tbl.rows.length;

	// Percorrendo a tabela
	for (var i = 0; i < tblLength; i++)
	{
		// Capturando linha da tabela
		var tblRow = tbl.rows[i];

		// Capturando os valores da primeira e segunda colunas da tabela - empresa e segmento
		var compCode = tblRow.childNodes[0].innerHTML;
		var segmento = tblRow.childNodes[1].innerHTML;

		// Comparando os filtros selecionados pra empresa e segmento com os valores da tabela
		if(compCode == givenCompCode && segmento == givenSegmento)
		{
			// Capturando valor da terceira coluna da tabela - template
			template =  tblRow.childNodes[2].innerHTML;

			// Adicionando prefixo ao nome do template
			template = tptPrefixo+template;

			break;
		}
	}

	return template;
}

/**
* Javascript functions that are to be integrated into a web template and that
* are to be used later in the web application ALWAYS have to have the same function
* signature, i.e. the parameters that will be passed.
*
* @param currentState - a list of parameters that describe the state the web item
* @param defaultCommandSequence - the initially used sequence of commands that
*				   would have been used instead of the custom
*				   script.
**/
function executeJS_TRANSFER_STATE_R( givenTemplate, currentState, defaultCommandSequence ){
	//Note: information can be extracted using the parameter 'currentState'
	//	and 'defaultCommandSequence'. In either case create your own object
	//	of type 'sapbi_CommandSequence' that will be sent to the server.
	//	To extract specific values of parameters refer to the following
	//	snippet:
	//		var key = currentState.getParameter( PARAM_KEY ).getValue();
	//		alert( "Selected key: " + key );
	//
	//	('PARAM_KEY' refers to any parameter's name)
	//Create a new object of type sapbi_CommandSequence
	var commandSequence = new sapbi_CommandSequence();
	/*
	 * Create a new object of type sapbi_Command with the command named "TRANSFER_STATE"
     */
	var commandTRANSFER_STATE_1 = new sapbi_Command( "TRANSFER_STATE" );
	/* Create parameter TEMPLATE */
	var paramTEMPLATE = new sapbi_Parameter( "TEMPLATE", givenTemplate );
	commandTRANSFER_STATE_1.addParameter( paramTEMPLATE );

	/* End parameter TEMPLATE */

	/* Create parameter DATA_PROVIDER_REF_LIST */
	var paramDATA_PROVIDER_REF_LIST = new sapbi_Parameter( "DATA_PROVIDER_REF_LIST", "" );
	var paramListDATA_PROVIDER_REF_LIST = new sapbi_ParameterList();
	// Create parameter DATA_PROVIDER_REF
	var paramDATA_PROVIDER_REF1 = new sapbi_Parameter( "DATA_PROVIDER_REF", dpFiltroInicial );
	paramListDATA_PROVIDER_REF_LIST.setParameter( paramDATA_PROVIDER_REF1, 1 );
		// End parameter DATA_PROVIDER_REF!
	paramDATA_PROVIDER_REF_LIST.setChildList( paramListDATA_PROVIDER_REF_LIST );
	commandTRANSFER_STATE_1.addParameter( paramDATA_PROVIDER_REF_LIST );

	/* End parameter DATA_PROVIDER_REF_LIST */

	// Add the command to the command sequence
	commandSequence.addCommand( commandTRANSFER_STATE_1 );
	/*
	 * End command commandTRANSFER_STATE_1
     */
	//Send the command sequence to the server
    return sapbi_page.sendCommand( commandSequence );
}

/**
* Javascript functions that are to be integrated into a web template and that
* are to be used later in the web application ALWAYS have to have the same function
* signature, i.e. the parameters that will be passed.
*
* @param currentState - a list of parameters that describe the state the web item
* @param defaultCommandSequence - the initially used sequence of commands that
*				   would have been used instead of the custom
*				   script.
**/
function executeJS_SET_TEMPLATE_R( currentState, defaultCommandSequence ){
	//Note: information can be extracted using the parameter 'currentState'
	//	and 'defaultCommandSequence'. In either case create your own object
	//	of type 'sapbi_CommandSequence' that will be sent to the server.
	//	To extract specific values of parameters refer to the following
	//	snippet:
	//		var key = currentState.getParameter( PARAM_KEY ).getValue();
	//		alert( "Selected key: " + key );
	//
	//	('PARAM_KEY' refers to any parameter's name)
	//Create a new object of type sapbi_CommandSequence
	var commandSequence = new sapbi_CommandSequence();
	/*
	 * Create a new object of type sapbi_Command with the command named "SET_TEMPLATE"
     */
	var commandSET_TEMPLATE_1 = new sapbi_Command( "SET_TEMPLATE" );
	/* Create parameter TEMPLATE */
	var paramTEMPLATE = new sapbi_Parameter( "TEMPLATE", tptMain );
	commandSET_TEMPLATE_1.addParameter( paramTEMPLATE );

	/* End parameter TEMPLATE */

	// Add the command to the command sequence
	commandSequence.addCommand( commandSET_TEMPLATE_1 );
	/*
	 * End command commandSET_TEMPLATE_1
     */
	//Send the command sequence to the server
    return sapbi_page.sendCommand( commandSequence );
}