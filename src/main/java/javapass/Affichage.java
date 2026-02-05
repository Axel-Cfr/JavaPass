package javapass;

public class Affichage {
    public static final String GREEN = "\033[0;32m"; //Green

    public static void afficherBienvenue() {
        String[] logo = {
        "                                                                        ",
        "                                                                        ",
        "                                                                        ",
        "                                                                        ",
        "                                     i                                  ",
        "                                    +jI                                 ",
        "                                     (rf{'                              ",
        "                                   ^l '|rrt.                            ",
        "                                 .;tl   (rr^                            ",
        "                           .:]fj/]^  `l//-                              ",
        "                         ,tr)^    If1:                                  ",
        "                        `/rj      \\:                                    ",
        "                        .(rr}.                                          ",
        "                          :1rrj(_'                                      ",
        "                              .`:_trt?                                  ",
        "                                    Irf'                                ",
        "                                '{tfrj)                                 ",
        "                               '{                                       ",
        "                        .'';I!<_]{1((){]~l,'                             ",
        "                      >\\+I`              ^>))                           ",
        "                      fj/}+l,`'.      ''I<](rI                          ",
        "                      \\:   .^,:;IllII;:''`   )^                          ",
        "               .]/f/1^)I_~                  \\                           ",
        "              It'.~+.iri<]    ;-1)){+''      /.                          ",
        "              (><\\' [\\t<l1  [         '+    /'                          ",
        "             .f'\\l   ~r>;(  ]   [)|i  `+    /'                          ",
        "             ./'|>    /!:\\  ]  +t||t, `+    /                           ",
        "              ]1,/;   tII|  ]  [rrrrl ,<    t                           ",
        "               {}.<\\/jjIi(  ,|'.```` :(.   .t                           ",
        "                ;\\(]1jj;<}    !1]<~}},     '\\                           ",
        "                     `f,+_                 :(                           ",
        "                      /<                  '/]                           ",
        "                       ^|j]:'.      ..'']jf>                             ",
        "                             'Ii<+~i;.                                  ",
        "                                                                        ",                                                                        
        "                                                                        ",                                                                        
        };
        for (String lines : logo) {
            System.out.println(GREEN + lines);
        }
        System.out.println("Bienvenue sur JavaPass !");
        System.out.println("Created by Axel Chabot, Morgann Morvan and Octave Girault");
    }
}
