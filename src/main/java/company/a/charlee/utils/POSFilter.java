package company.a.charlee.utils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class POSFilter {

    public List<String> filterSignificantPOS(List<String> tokens) {
        return tokens.stream()
                .filter(token -> !conjunctionsSet.contains(token.toLowerCase()))
                .filter(token -> !prepositionsSet.contains(token.toLowerCase()))
                .filter(token -> !pronounsSet.contains(token.toLowerCase()))
                .filter(token -> !exclamationsSet.contains(token.toLowerCase()))
                .collect(Collectors.toList());
    }

    private final Set<String> conjunctionsSet =
            Stream.of(
                    "і", "й", "та", "ні", "ані", "не", "тільки", "але", "так", "а", "проте", "зате", "однак",
                    "або", "чи", "хоч", "то", "тим", "того", "відтоді", "тих", "пір", "тимчасово",
                    "міру", "тільки-но", "тільки", "скоро", "ледве", "щойно", "бо", "тому",
                    "те", "тим", "оскільки", "позаяк", "щоб", "задля", "аби", "якщо", "якби", "б",
                    "раз", "хоч", "хоча", "хай", "нехай", "дарма", "незважаючи", "що", "хто", "де", "коли", "чому", "як"
            ).collect(Collectors.toSet());

    private final Set<String> prepositionsSet =
            Stream.of(
                    "без", "біля", "в", "від", "для", "до", "з", "за", "із", "крізь", "між", "на", "над",
                    "о", "об", "по", "під", "про", "перед", "при", "через", "у", "щодо", "наріж", "навколо",
                    "впродовж", "внаслідок", "згідно", "завдяки", "попри", "вздовж", "наперекір", "наразі",
                    "поза", "серед", "поміж", "проти", "ради", "скрізь", "супроти", "понад", "після", "ще"
            ).collect(Collectors.toSet());

    private final Set<String> pronounsSet =
            Stream.of(
                    "я", "ти", "він", "вона", "воно", "ми", "ви", "вони",
                    "мене", "мій", "твій", "його", "її", "наш", "ваш", "їх",
                    "мені", "тобі", "йому", "їй", "нам", "вам", "їм",
                    "мною", "тобою", "ним", "нею", "нами", "вами", "ними",
                    "себе", "свій",
                    "цей", "ця", "це", "ці",
                    "той", "та", "те", "ті",
                    "котрий", "який", "чий",
                    "нічий", "ніякий", "ніхто", "ніде", "ніколи", "нічого", "ніяк",
                    "деякий", "кожний", "сам", "сама", "саме", "самі", "будь",
                    "хтось", "щось", "чомусь", "такий"
            ).collect(Collectors.toSet());

    private final Set<String> exclamationsSet =
            Stream.of(
                    "ах", "ох", "ех", "ура", "гей", "ой", "ай", "але", "так", "ні", "гоп", "хоп",
                    "фу", "цить", "ша", "га", "отакої", "аве", "ізі", "капець", "опа", "ха", "ха-ха", "хе-хе",
                    "хі-хі", "хо-хо", "ага", "агов", "ба", "бе", "гопс",
                    "йой", "йохохо", "куку", "мда", "о", "пф", "та", "фі", "хм", "чи", "шух","ось"
            ).collect(Collectors.toSet());

}
