package mytest;



public class Morpheme {
	   String a;
	   String yomi;
	   String kihonkei;
	   String hinshi;
	   public boolean equals(Morpheme m){
		   return (this.kihonkei.equals(m.kihonkei)&&this.hinshi.equals(m.hinshi));
	   }
	}

