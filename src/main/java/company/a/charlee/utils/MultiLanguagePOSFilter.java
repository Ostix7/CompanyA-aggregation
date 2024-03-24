package company.a.charlee.utils;

import opennlp.tools.postag.POSTaggerME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MultiLanguagePOSFilter {

    private final POSTaggerME enPOSFilter;

    public MultiLanguagePOSFilter(POSTaggerME enPOSFilter) {
        this.enPOSFilter = enPOSFilter;
    }

    public List<String> filterSignificantPOS(List<String> tokens, DetectedLanguage language) {
        List<String> filteredPOS;
        switch (language) {
            case ENGLISH:
                String[] tags = enPOSFilter.tag(tokens.toArray(new String[0]));
                List<String> result = new ArrayList<>();
                for (int i = 0; i < tags.length; i++) {
                    if (tags[i].equals("NOUN") || tags[i].equals("PROPN") || tags[i].equals("NOUN+PART") || tags[i].equals("PROPN+PART")) {
                        result.add(tokens.get(i));
                    }
                }
                filteredPOS = result;
                break;
            case UKRAINIAN:
                filteredPOS = filterUkrainianPOS(tokens);
                break;
            case RUSSIAN:
                filteredPOS = filterRussianPOS(tokens);
                break;
            default:
                filteredPOS = new ArrayList<>();
                break;
        }
        return filteredPOS;
    }

    private List<String> filterUkrainianPOS(List<String> tokens) {
        return tokens.stream()
                .filter(token -> !conjunctionsUkrSet.contains(token.toLowerCase()))
                .filter(token -> !prepositionsUkrSet.contains(token.toLowerCase()))
                .filter(token -> !pronounsUkrSet.contains(token.toLowerCase()))
                .filter(token -> !exclamationsUkrSet.contains(token.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<String> filterRussianPOS(List<String> tokens) {
        return tokens.stream()
                .filter(token -> !conjunctionsRusSet.contains(token.toLowerCase()))
                .filter(token -> !prepositionsRusSet.contains(token.toLowerCase()))
                .filter(token -> !pronounsRusSet.contains(token.toLowerCase()))
                .filter(token -> !exclamationsRusSet.contains(token.toLowerCase()))
                .collect(Collectors.toList());
    }

    private final Set<String> conjunctionsUkrSet =
            Stream.of(
                    "і", "й", "та", "ні", "ані", "не", "тільки", "але", "так", "а", "проте", "зате", "однак",
                    "або", "чи", "хоч", "то", "тим", "того", "відтоді", "тих", "пір", "тимчасово",
                    "міру", "тільки-но", "тільки", "скоро", "ледве", "щойно", "бо", "тому",
                    "те", "тим", "оскільки", "позаяк", "щоб", "задля", "аби", "якщо", "якби", "б",
                    "раз", "хоч", "хоча", "хай", "нехай", "дарма", "незважаючи", "що", "хто", "де", "коли", "чому", "як"
            ).collect(Collectors.toSet());

    private final Set<String> prepositionsUkrSet =
            Stream.of(
                    "без", "біля", "в", "від", "для", "до", "з", "за", "із", "крізь", "між", "на", "над",
                    "о", "об", "по", "під", "про", "перед", "при", "через", "у", "щодо", "наріж", "навколо",
                    "впродовж", "внаслідок", "згідно", "завдяки", "попри", "вздовж", "наперекір", "наразі",
                    "поза", "серед", "поміж", "проти", "ради", "скрізь", "супроти", "понад", "після", "ще"
            ).collect(Collectors.toSet());

    private final Set<String> pronounsUkrSet =
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

    private final Set<String> exclamationsUkrSet =
            Stream.of(
                    "ах", "ох", "ех", "ура", "гей", "ой", "ай", "але", "так", "ні", "гоп", "хоп",
                    "фу", "цить", "ша", "га", "отакої", "аве", "ізі", "капець", "опа", "ха", "ха-ха", "хе-хе",
                    "хі-хі", "хо-хо", "ага", "агов", "ба", "бе", "гопс",
                    "йой", "йохохо", "куку", "мда", "о", "пф", "та", "фі", "хм", "чи", "шух","ось"
            ).collect(Collectors.toSet());

    private final Set<String> conjunctionsRusSet =
            Stream.of(
                    "и", "а", "но", "зато", "однако", "же", "либо", "или", "то", "также", "потому", "что", "если",
                    "хотя", "когда", "пока", "дабы", "чтобы", "раз", "даже", "если", "несмотря"
            ).collect(Collectors.toSet());

    private final Set<String> prepositionsRusSet =
            Stream.of(
                    "в", "на", "под", "над", "к", "перед", "за", "о", "об", "от", "по", "из", "у", "до", "без", "для",
                    "вокруг", "сквозь", "через", "между", "после", "вне", "внутри", "с", "среди", "около", "против"
            ).collect(Collectors.toSet());

    private final Set<String> pronounsRusSet =
            Stream.of(
                    "я", "ты", "он", "она", "оно", "мы", "вы", "они",
                    "мой", "твой", "его", "её", "наш", "ваш", "их",
                    "кто", "что", "какой", "какая", "какое", "какие", "как",
                    "себя", "свой", "чей", "этот", "тот", "каждый", "все", "некоторый", "любой", "сам", "себе"
            ).collect(Collectors.toSet());

    private final Set<String> exclamationsRusSet =
            Stream.of(
                    "ах", "ох", "эх", "ура", "ой", "эй", "алло", "вот", "гоп", "да", "нет", "ну",
                    "опа", "оу", "фу", "ха", "что", "эге", "эй", "яй", "юху", "ай", "охо-хо", "ау"
            ).collect(Collectors.toSet());


}
