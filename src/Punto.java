public class Punto{
		private String idN4J;
		public String lat;
		public String lng;
		public int appears;
		public Punto(String lat, String lng, int appears) {
			super();
			this.lat = lat;
			this.lng = lng;
			this.appears = appears;
		}
		
		public Punto(String lat, String lng, int appears, String idN4J ) {
			super();
			this.idN4J = idN4J;
			this.lat = lat;
			this.lng = lng;
			this.appears = appears;
		}

		@Override
		public String toString() {
			return "Punto [idN4J=" + idN4J + ", lat=" + lat + ", lng=" + lng + ", appears=" + appears + "]";
		}

		public String getIdN4J() {
			return idN4J;
		}
		public void setIdN4J(String idN4J) {
			this.idN4J = idN4J;
		}
		
	}