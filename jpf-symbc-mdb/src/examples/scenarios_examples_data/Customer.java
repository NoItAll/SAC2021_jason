package scenarios_examples_data;

public class Customer {
	
	private String id;
	private double income;
	private Integer status;
	protected Customer enlistedBy;
	
	public Customer() {}

	public String getId() {
		return id;
	}

	public Customer setId(String id) {
		this.id = id;
		return this;
	}

	public double getIncome() {
		return income;
	}

	public Customer setIncome(double income) {
		this.income = income;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public Customer setStatus(Integer status) {
		this.status = status;
		return this;
	}
	
	public Customer getEnlistedBy() {
		return enlistedBy;
	}
	
	public void setEnlistedBy(Customer enlistedBy) {
		this.enlistedBy = enlistedBy;
	}
	
	
	@Override
	public String toString() {
		return "Customer{id="+id+",income="+income+",status="+status+"}";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Customer)) {
			return false;
		} else {
			Customer c = (Customer) o;
			boolean idEquals;
			if (id != null) {
				idEquals = id.equals(c.id);
			} else {
				idEquals = c.id == null;
			}
			boolean statusEquals;
			if (status != null) {
				statusEquals = status.equals(c.status);
			} else {
				statusEquals = c.status == null;
			}
			return idEquals && 
					income == c.income && 
					statusEquals;
		}
	}
}
