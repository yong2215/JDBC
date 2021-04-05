import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

class student {
	int sid;
	String sname;
	String deptno;
	String advisor;
	String gen;
	String addr;
	String birthdate;
	String grade;
	
	public int getId() {
		return sid;
	}
	public void setId(int sid) {
		this.sid = sid;
	}
	public String getName() {
		return sname;
	}
	public void setName(String sname) {
		this.sname = sname;
	}
	public String getDeptno() {
		return deptno;
	}
	public void setDeptno(String deptno) {
		this.deptno=deptno;
	}
	public String getAdvisor() {
		return advisor;
	}
	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}
	public String getGen() {
		return gen;
	}
	public void setGen(String gen) {
		this.gen = gen;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGrade() {
		return grade;
	}
}

class join_table{
	int sid;
	String name;
	String dname;
	String pname;
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid= sid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
}

public class JDBCTest {
	final static String jdbc_driver = "oracle.jdbc.driver.OracleDriver";
	final static String jdbc_url = "jdbc:oracle:thin:@dbserver.yu.ac.kr:1521:XE";
	static Connection conn = null;
	static PreparedStatement pstmt = null;
	
	static void connect() {
		try {
			Class.forName(jdbc_driver).newInstance();
			conn = DriverManager.getConnection(jdbc_url, "student227", "21510734");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	static void disconnect() {
		try {
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static boolean insertItem(student item) {
		String sql = "insert into student(sid, sname, deptno, advisor, gen, addr, birthdate, grade) values(?,?,?,?,?,?,to_date(?,'yyyy/mm/dd'),?)";
		int result = 0;
		
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, item.getId());
			pstmt.setString(2, item.getName());
			pstmt.setString(3, item.getDeptno());
			pstmt.setString(4, item.getAdvisor());
			pstmt.setString(5, item.getGen());
			pstmt.setString(6, item.getAddr());
			pstmt.setString(7, item.getBirthdate());
			pstmt.setString(8, item.getGrade());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return (result == 1) ? true : false;
	}
	
	public static boolean updateItem(student item) {
		String sql = "update student set sname=?, deptno=?, advisor=?, gen=?, addr=?, birthdate=?, grade=? where sid=?";
		int result = 0;
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, item.getName());
			pstmt.setString(2, item.getDeptno());
			pstmt.setString(3, item.getAdvisor());
			pstmt.setString(4, item.getGen());
			pstmt.setString(5, item.getAddr());
			pstmt.setString(6, item.getBirthdate());
			pstmt.setString(7, item.getGrade());
			pstmt.setInt(8, item.getId());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return (result == 1) ? true : false;
	}
	
	public static boolean deleteItem(int id) {
		String sql = "delete from student where sid=?";
		int result = 0;
		
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return (result == 1) ? true : false;
	}
	
	public static student getItem(int id) {
		String sql = "select sname, deptno, advisor, gen, addr, birthdate, grade from student where sid=?";
		student item = new student();

		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				item.setId(id);
				item.setName(rs.getString("sname"));
				item.setDeptno(rs.getString("deptno"));
				item.setAdvisor(rs.getString("advisor"));
				item.setGen(rs.getString("gen"));
				item.setAddr(rs.getString("addr"));
				item.setBirthdate(rs.getString("birthdate"));
				item.setGrade(rs.getString("grade"));
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return item;
	}
	
	public static Iterable<join_table> getList(int find) {
		String sql = "select sid, sname, dname, pname from student s, department d, professor p where s.deptno = d.deptno and advisor = pid and s.deptno = ?";
		ArrayList<join_table> list = new ArrayList<>();
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, find);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				join_table item = new join_table();
				item.setSid(rs.getInt("sid"));
				item.setName(rs.getString("sname"));
				item.setDname(rs.getString("dname"));
				item.setPname(rs.getString("pname"));
				list.add(item);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return list;
	}
	
	public static boolean findSid(int id) {
		String sql = "select sid from student";
		int result=0;
		student item = new student();
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				item.setId(rs.getInt("sid"));
				if(id == item.getId()) {
					result = 1;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return (result == 1) ? true : false;
	}
	
	public static boolean findDeptno(String deptno) {
		String sql = "select deptno from student";
		int result=0;
		student item = new student();
		connect();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				item.setDeptno(rs.getString("deptno"));
				if(deptno.equals(item.getDeptno())) {
					result = 1;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
		return (result == 1) ? true : false;
	}
	
	
	
	public static void insert_list(student st) {
		Scanner in = new Scanner(System.in);
		System.out.println("�й��� �Է��Ͻÿ�(0�� ����������ÿ�) : ");
		st.setId(in.nextInt());
		in.nextLine();
		System.out.println("�̸��� �Է��Ͻÿ� : ");
		st.setName(in.nextLine());
		System.out.println("�а���ȣ�� �Է��Ͻÿ� : ");
		st.setDeptno(in.nextLine());
		System.out.println("������ȣ�� �Է��Ͻÿ� : ");
		st.setAdvisor(in.nextLine());
		System.out.println("������ �Է��Ͻÿ�(M or F) : ");
		st.setGen(in.nextLine());
		System.out.println("�ּҸ� �Է��Ͻÿ� : ");
		st.setAddr(in.nextLine());
		System.out.println("��������� �Է��Ͻÿ� : ");
		st.setBirthdate(in.nextLine());
		System.out.println("������ �Է��Ͻÿ� : ");
		st.setGrade(in.nextLine());
	}
	
	public static void update_list(student st) {
		 Scanner in = new Scanner(System.in);
		 System.out.println("������ �̸��� �Է��ϼ���.");
		 st.setName(in.nextLine());
		 System.out.println("������ �а���ȣ�� �Է��ϼ���.");
		 st.setDeptno(in.nextLine());
		 System.out.println("������ ������ȣ�� �Է��ϼ���.");
		 st.setAdvisor(in.nextLine());
		 System.out.println("������ ������ �Է��ϼ��� (M or F)");
		 st.setGen(in.nextLine());
		 System.out.println("�ּҸ� �Է��ϼ���.");
		 st.setAddr(in.nextLine());
		 System.out.println("��������� �Է��ϼ���.(yyyy/mm/dd)");
		 st.setBirthdate(in.nextLine());
		 System.out.println("������ �Է��ϼ���.");
		 st.setGrade(in.nextLine());
	}

	public static void print_info(student st_2) {
		System.out.println("�й� : "+st_2.getId());
		System.out.println("�̸� : "+st_2.getName());
		System.out.println("�а���ȣ : "+st_2.getDeptno());
		System.out.println("������ȣ : "+st_2.getAdvisor());
		System.out.println("���� : "+st_2.getGen());
		System.out.println("�ּ� :"+st_2.getAddr());
		System.out.println("������� : "+(st_2.getBirthdate()));
		System.out.println("���� : "+st_2.getGrade());
		System.out.println();
	}
	
	public static void print_info_1(join_table t) {
		System.out.println(t.getSid()+"	"+t.getName()+"	"+t.getDname()+"	"+t.getPname());
	}
	
	public static void print_info_2(student st_2) {
		System.out.println("�й� : "+st_2.getId());
		System.out.println("�̸� : "+st_2.getName());
		System.out.println("�а���ȣ : "+st_2.getDeptno());
		System.out.println("������ȣ : "+st_2.getAdvisor());
		System.out.println("���� : "+st_2.getGen());
		System.out.println("�ּ� :"+st_2.getAddr());
		if(st_2.getBirthdate() != null)
			System.out.println("������� : "+(st_2.getBirthdate()).substring(0, 10));
		else
			System.out.println("������� : "+(st_2.getBirthdate()));
		System.out.println("���� : "+st_2.getGrade());
		System.out.println();
	}
	
	public static void main(String[] args) {
		while(true) {
			Scanner in = new Scanner(System.in);
			System.out.println("Menu(1: �л� �߰�, 2: �л� ����, 3: �л� ����, 4: �л� �˻�, 5: �а� ���, 6: ����)");
			int select = in.nextInt();
			switch(select) {
				case 1:
					try {
						student st = new student();
						insert_list(st);
						print_info(st);
						if (insertItem(st))
							System.out.println("1���� �߰��Ǿ����ϴ�.");
						else 
							System.out.println("�߰��� �����߽��ϴ�.");
					}catch (Exception e) {
						System.out.println(e);
						System.out.println("���� �ش��ϴ� ������ �߻��Ͽ����ϴ�.\n");
					}
					break;
				case 2:
					while(true) {
						System.out.print("������ �й��� �Է��Ͻÿ�(00 �Է½� �޴��� �̵��մϴ�) : ");
						int i = in.nextInt();
						if(i != 00) {
							if(deleteItem(i))
							System.out.println("�����Ǿ����ϴ�.");
							else 
								System.out.println("�����߽��ϴ�.");
						}
						else {
							System.out.println();
							break;
						}
					}
					break;
				case 3:
					try {
						while(true) {
							System.out.print("�˻��� �л��� �й��� �Է��Ͻÿ�(00 �Է½� �޴��� �̵��մϴ�) : ");
							student st_1 = new student();
							int i = in.nextInt();
							if(i != 00) {
								if(!findSid(i)) {
									System.out.println("���������� �й��� �л��� �����ϴ�.");
									break;
								}
								else {
									st_1 = getItem(i);
									print_info_2(st_1);
									update_list(st_1);
									if(updateItem(st_1)) 
										System.out.println("�����Ǿ����ϴ�.");
									else 
										System.out.println("������ �����߽��ϴ�.");
								}
							}
							else {
								System.out.println();
								break;
							}
						}	
					}catch (Exception e) {
						System.out.println(e);
						System.out.println("���� �ش��ϴ� ������ �߻��Ͽ����ϴ�.\n");
					}
					break;
				case 4:
					try {
						while(true) {
							System.out.print("������ �л��� �й��� �Է��Ͻÿ�(00 �Է½� �޴��� �̵��մϴ�) : ");
							student st_3 = new student();
							int i = in.nextInt();
							if(i != 00) {
								st_3 = getItem(i);
								if(st_3.getId() != 0)
									print_info_2(st_3);
								else
									System.out.println("�ش��й��� �л��� �����ϴ�.");
							}
							else 
								break;
						}
					}catch (Exception e) {
						System.out.println(e);
						System.out.println("���� �ش��ϴ� ������ �߻��Ͽ����ϴ�.\n");
					}
					break;
				case 5:
					in.nextLine();
					while(true) {
						System.out.print("ã�� ���� �а� ��ȣ�� �Է��Ͻÿ�(exit �Է½� �޴��� �̵��մϴ�) : ");
						String find = in.nextLine();						
						if(!(find.equals("exit"))) {
							if(findDeptno(find)) {
								Iterator<join_table> iterator = getList(Integer.parseInt(find)).iterator();
								System.out.println("�й�	�л��̸�	�а��̸�		���������̸�");
								while(iterator.hasNext()) {
									join_table element = iterator.next();
									print_info_1(element);
								}
								System.out.println();
							}
							else
								System.out.println("ã������ �а��� ���������ʽ��ϴ�.");
						}
						else {
							System.out.println();
							break;
							}
					}
					break;
				case 6:
					System.out.println("�����մϴ�");
					return;
			}
		}
	}

}
