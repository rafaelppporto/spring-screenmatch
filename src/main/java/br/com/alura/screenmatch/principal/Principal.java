package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();

    public static final String ENDERECO = "https://www.omdbapi.com/";
    public static String API_KEY = "?apikey=52bb4727";

    public void exibeMenu() {
        System.out.println("Digite o nome da serie");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + API_KEY + "&t=" + nomeSerie.replace(" ", "+"));
        DadosSerie dados = converteDados.obterDados(json, DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            String uriRequest = ENDERECO+API_KEY+ "&t=" + nomeSerie.replace(" ", "+") + "&season=" + i;
            json = consumoApi.obterDados(uriRequest);
            DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println("S" + t.numero() + "E" + e.numeroEpisodio() + " - " + e.titulo())));
    }
}