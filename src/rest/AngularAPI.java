package rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/angularAPI")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AngularAPI 
{	
	private static String pathImagemCorrigido;
	private static File arquivoOriginal = null;
	private static byte[] byteArrayArquivoCopiado = null;

	@GET
	@Path("/reloadImagem/{param : .*}")
	public Response printMessage(@PathParam("param") String pathImagem) 
	{
		System.out.println(pathImagem);
		try 
		{
			padronizarPath(pathImagem);
			existeArquivo();
			gerarBinarioArquivo();
		} 
		catch (IndexOutOfBoundsException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidPathException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return Response
				.status(200)
				.entity(Base64.getEncoder().encodeToString(byteArrayArquivoCopiado))
				.build();
	}

	private static void padronizarPath(String pathImagem) 
		throws IndexOutOfBoundsException
	{
		if(pathImagem != null && !pathImagem.isEmpty()) 
		{
			if(pathImagem.contains("//")) 
			{
				pathImagemCorrigido = pathImagem.replace("//", File.separator);
			}
			else if(pathImagem.indexOf('/') != -1) 
			{
				pathImagemCorrigido = pathImagem.replace("/", File.separator);
			}
			else
			{
				pathImagemCorrigido = pathImagem;
			}
			
			System.out.println(pathImagemCorrigido);
		}
		else 
		{
			throw new StringIndexOutOfBoundsException("Caminho do arquivo informado é nulo ou vazio!");
		}
	}

	private static void existeArquivo() 
		throws FileNotFoundException 
	{
		arquivoOriginal = new File(pathImagemCorrigido);

		if (arquivoOriginal == null || !arquivoOriginal.exists()) 
		{
			throw new FileNotFoundException("Arquivo não encontrado!");
		}
		if(!arquivoOriginal.isFile()) 
		{
			throw new FileNotFoundException("O caminho informado não é de um arquivo válido!");
		}
		if(!arquivoOriginal.canRead()) 
		{
			throw new FileNotFoundException("O arquivo encontrado está corrompido!");
		}
		
		System.out.println("Arquivo encontrado: "+arquivoOriginal.getPath());
	}

	private static void gerarBinarioArquivo() 
		throws InvalidPathException, IOException 
	{	
		java.nio.file.Path path = Paths.get(arquivoOriginal.getPath());
		byteArrayArquivoCopiado = Files.readAllBytes(path);
		//byteArrayArquivoCopiado = Base64.getEncoder().encode(Files.readAllBytes(path));			
	}

}
