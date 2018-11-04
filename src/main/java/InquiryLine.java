import java.io.IOException;

class InquiryLine {

    private static String text;
    private static String k_num = "";
    private static String[] tech;

    InquiryLine(String text, String s1) {
        this.text = text;
      // System.out.println( text);
    }

    int compare(int[] nums) throws IOException, InterruptedException {
        String p0 = "clmns-";
        tech = new String[20];
        int[] n = { 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14};
        int k = 1;
        tech[1] = "&+|Л" + text.substring(text.indexOf('-'), text.indexOf('<'));       // номер наряда


        for(int i : nums) {
            if(  text.indexOf(p0, text.indexOf(p0 + i ) + 1) > 0  & text.indexOf( p0 + i) > 0 & i < 30) {
                tech[n[k++]] = form_title(text.substring(text.indexOf(p0 + i), text.indexOf(p0, text.indexOf(p0 + i) + 1)).replace("|", ""));
            }
            if(i == 30)
                tech[n[k++]] = form_title( text.substring( text.indexOf(p0 + i) , text.indexOf("div" , text.indexOf(p0 + i) + 1) ).replace("|", ""));
        }


        tech[5] = "#" + k_num + " " + tech[5];
        StoreInquiry.list.add(tech);
        return text.length();
    }

    private static String form_title(String s) throws InterruptedException {
        int i = 0;
        switch (s.substring(0,s.indexOf('_'))) {
            case "clmns-1-j":
                String r = s.substring(s.indexOf("title=") + 7, s.indexOf('"', s.indexOf("title=") + 7));
                tech[0] = s.substring(s.indexOf("title=") + 7 , s.indexOf("title=") + 12 ); // Код города
                return  r.substring(4);
            case "clmns-3-j":
                if(!s.contains("КВ"))
                    for(int is = 4; is < 7; is++)
                        if(text.contains("clmns-" + is)) {
                            System.out.println("yes=" + is);
                            s = text.substring(text.indexOf("clmns-" + is));
                            break;
                        }

                if( s.contains("СКВ"))
                    return "(" + s.substring(s.indexOf("СКВ"), s.indexOf(')', s.indexOf("СКВ"))).replace("&nbsp;", "").replace("задачи", "") + ")";
                else
                    return "(" +  s.substring(s.indexOf("КВ"), s.indexOf(')', s.indexOf("КВ"))).replace("&nbsp;", "").replace("задачи", "") + ")";
            //case "clmns-15-j":  return s.substring(s.indexOf("с "), s.indexOf('<' , s.indexOf("с "))) ;
            case "clmns-26-j":
                tech[9] = "";
                String tel = "";
                for( int n = 0;  s.indexOf("title", i) != -1 ; n++) {
                    if( n == 0 )
                        if (InquiryShpd.bol.func("", s.substring(s.indexOf("title=") + 7, s.indexOf('"', s.indexOf("title=") + 7))))
                            tel = s.substring(s.indexOf("title=") + 7, s.indexOf('"', s.indexOf("title=") + 7));
                        else {
                            tel = "-нет номера";
                            tech[9] = s.substring(s.indexOf("title=") + 7, s.indexOf('"', s.indexOf("title=") + 7));
                        }
                    else
                        tech[9] +=  s.substring(s.indexOf("title", i) + 7 , s.indexOf('"', s.indexOf("title", i) + 8) ).replace("\n", "");
                    i = s.indexOf("title", i) + 1;
                }
                return "+7" + tel ;
            case "clmns-18-j":
                String work = "";
                for(int n = 0; n < 3 ; n++) {
                    if( n == 2)
                        work = s.substring(s.indexOf("title", i) + 7 , s.indexOf('"', s.indexOf("title", i) + 8) );
                    if(work.contains("Время отображено"))
                        work="  ";
                    else
                        i = s.indexOf("title", i) + 1;
                }
                tech[2] = s.substring(s.indexOf("с ", i), s.indexOf('<' , s.indexOf("с ", i)));
                return work;
            case "clmns-29-j":
                k_num = s.substring(s.indexOf("title=") + 8, s.indexOf(" ", s.indexOf("title=") + 10));
                if (k_num.length() < 3) k_num += ' ';
                tech[19] = k_num ;
                return s.substring(s.indexOf("title=") + 7 , s.indexOf( "</span>", s.indexOf("title=") + 7)).replace("&quot;", "");
            case "clmns-30-j":
                for( int n = 0;  s.indexOf("title", i) != -1 ; n++) {
                    if( k_num.contains("нет")) {
                        k_num = s.substring(s.indexOf("title=") + 8, s.indexOf(" ", s.indexOf("title=") + 10));
                        if (k_num.length() < 3) k_num += ' ';
                        tech[19] = k_num;
                        tech[11] = s.substring(s.indexOf("title=") + 7 , s.indexOf( "</span>", s.indexOf("title=") + 7)).replace("&quot;", "");
                        tech[10] = "нет";
                    }
                    else
                        return  s.substring(s.lastIndexOf("title") + 7,  s.indexOf( '"', s.lastIndexOf("title") + 8)).replace("\n", "");     // Примчание
                }
            default:
                if(s.indexOf("title") > 0 )
                    return s.substring(s.indexOf("title=") + 7 , s.indexOf( '"', s.indexOf("title=") + 7)).replace("&quot;", "");
                else
                    return " ";
        }
    }
}
