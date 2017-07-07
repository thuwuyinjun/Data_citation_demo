package edu.upenn.cis.citation.stress_test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.json.JSONException;

import edu.upenn.cis.citation.Corecover.Query;
import edu.upenn.cis.citation.Corecover.Subgoal;
import edu.upenn.cis.citation.Pre_processing.populate_db;
import edu.upenn.cis.citation.citation_view.Head_strs;
import edu.upenn.cis.citation.citation_view.citation_view_vector;
import edu.upenn.cis.citation.datalog.Query_converter;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning1;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning1_citation_opt;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning1_citation_opt2;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning1_test;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning2;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning2_citation_opt;
import edu.upenn.cis.citation.reasoning.Tuple_reasoning2_test;
import edu.upenn.cis.citation.user_query.query_storage;

public class stress_test3 {
	
	static int size_range = 100;
	
	static int times = 1;
	
	static int size_upper_bound = 5;
	
	static int num_views = 3;
	
	static int view_max_size = 40;
	
	static int upper_bound = 20;
	
	static int query_num = 8;
	
	static boolean store_covering_set = true;
	
	static Vector<String> get_unique_relation_names(Query query)
	{
		Vector<String> relation_names = new Vector<String>();
		
		HashSet<String> relations = new HashSet<String>();
		
		for(int i = 0; i<query.body.size(); i++)
		{
			Subgoal subgoal = (Subgoal) query.body.get(i);
			
			relations.add(query.subgoal_name_mapping.get(subgoal.name));
			
		}
		
		relation_names.addAll(relations);
		
		return relation_names;
		
	}
	
	static Vector<String> get_relation_names(Query query)
	{
		Vector<String> relation_names = new Vector<String>();
				
		for(int i = 0; i<query.body.size(); i++)
		{
			Subgoal subgoal = (Subgoal) query.body.get(i);
			
			relation_names.add(query.subgoal_name_mapping.get(subgoal.name));
			
		}
				
		return relation_names;
		
	}
	
	public static void main(String [] args) throws ClassNotFoundException, SQLException, IOException, InterruptedException, JSONException
	{
		
//		reset();
		
		Connection c = null;
	      PreparedStatement pst = null;
		Class.forName("org.postgresql.Driver");
	    c = DriverManager
	        .getConnection(populate_db.db_url, populate_db.usr_name , populate_db.passwd);
		
		
		
				
//		Query query = query_storage.get_query_by_id(1);
		
		for(int k = 3; k<=query_num; k++)
		{
			reset(c, pst);
			
			System.out.println("finish resetting");
			
//			Query query = query_storage.get_query_by_id(1);
			
			Query query = query_generator.gen_query(k, c, pst);
			
			query_storage.store_query(query, new Vector<Integer>());
			
			System.out.println(query);
			
			stress_testing(query, c, pst);

		}
		

		
		
		
		c.close();
		
		
	}
	
	static void stress_testing(Query query, Connection c, PreparedStatement pst) throws ClassNotFoundException, SQLException, IOException, InterruptedException, JSONException
	{
		
		HashMap<Head_strs, HashSet<String> > citation_strs = new HashMap<Head_strs, HashSet<String>>();
		
		HashMap<Head_strs, HashSet<String> > citation_strs2 = new HashMap<Head_strs, HashSet<String>>();

		
		String f_name = new String();
		
		HashMap<Head_strs, Vector<Vector<citation_view_vector>>> citation_view_map1 = new HashMap<Head_strs, Vector<Vector<citation_view_vector>>>();

		HashMap<Head_strs, Vector<Vector<citation_view_vector>>> citation_view_map2 = new HashMap<Head_strs, Vector<Vector<citation_view_vector>>>();
		
		Vector<Vector<citation_view_vector>> citation_view1 = new Vector<Vector<citation_view_vector>>();

		Vector<Vector<citation_view_vector>> citation_view2 = new Vector<Vector<citation_view_vector>>();
		
		Vector<String> relations = get_unique_relation_names(query);
		
		Vector<Query> views = view_generator.gen_default_views(relations);
		
		for(int i = 0; i<views.size(); i++)
		{
			System.out.println(views.get(i));
		}
		
//		HashSet<Query> views = view_generator.generate_store_views(relation_names, num_views, query.body.size(), query);
		
		int view_size = 2;
		
		int view_id = views.size() + 1;
		
		System.out.println("start");
		
		double end_time = 0;

		
		double start_time = System.nanoTime();
		
		
		for(int k = 0; k<times; k++)
		{
			
			citation_view_map1.clear();
			
			citation_strs.clear();
						
			Tuple_reasoning1_test.tuple_reasoning(query, citation_strs,  citation_view_map1, c, pst);
			
			System.gc();
			
		}
		
		Vector<String> agg_citations1 = Tuple_reasoning1_test.tuple_gen_agg_citations(query);
		
		end_time = System.nanoTime();
		
		double time = (end_time - start_time);
		
		time = time /(times * 1.0 *1000000000);
		
		System.out.print(time + "s	");
		
		Set<Head_strs> h_l = citation_strs.keySet();
		
		double citation_size1 = 0;
		
		int row = 0;
		
		for(Iterator iter = h_l.iterator(); iter.hasNext();)
		{
			Head_strs h_value = (Head_strs) iter.next();
			
			HashSet<String> citations = citation_strs.get(h_value);
			
			citation_size1 += citations.size();
			
			row ++;
			
		}
		
		if(row !=0)
		citation_size1 = citation_size1 / row;
		
		System.out.print(citation_size1 + "	");
		
		Set<Head_strs> head = citation_view_map1.keySet();
		
		int row_num = 0;
		
		double origin_citation_size = 0.0;
		
		for(Iterator iter = head.iterator(); iter.hasNext();)
		{
			Head_strs head_val = (Head_strs) iter.next();
			
			Vector<Vector<citation_view_vector>> c_view = citation_view_map1.get(head_val);
			
			row_num++;
			
			for(int p = 0; p<c_view.size(); p++)
			{
				origin_citation_size += c_view.get(p).size();
			}
			
		}
		
		
		
		if(row_num !=0)
			origin_citation_size = origin_citation_size / row_num;
		
		System.out.print(row_num + "	");
		
		System.out.print(origin_citation_size + "	");
		
		
		citation_view_map1.clear();
		
		citation_strs.clear();
		
		citation_view1.clear();
		
		
		
		
		
		start_time = System.nanoTime();
		
		for(int k = 0; k<times; k++)
		{
			citation_view_map2.clear();
			
			citation_strs2.clear();
			
			citation_view2.clear();
			
			citation_view2 = Tuple_reasoning2_test.tuple_reasoning(query, citation_strs2, citation_view_map2, c, pst);
			
			System.gc();
		}
		
		end_time = System.nanoTime();
		
		Vector<String> agg_citations2 = Tuple_reasoning2_test.tuple_gen_agg_citations(citation_view2);
		
		System.out.println(agg_citations2);
		
		
		time = (end_time - start_time);
		
		
		time = time /(times * 1.0 *1000000000);
		
		System.out.print(time + "s	");
		
		
		h_l = citation_strs2.keySet();
		
		double citation_size2 = 0.0;
		
		row = 0;
		
		for(Iterator iter = h_l.iterator(); iter.hasNext();)
		{
			Head_strs h_value = (Head_strs) iter.next();
			
			HashSet<String> citations = citation_strs2.get(h_value);
			
			citation_size2 += citations.size();
			
			row ++;
			
		}
		
		if(row !=0)
			citation_size2 = citation_size2 / row;

		System.out.print(citation_size2 + "	");

		
		head = citation_view_map2.keySet();
		
		row_num = 0;
		
		origin_citation_size = 0.0;
		
		for(Iterator iter = head.iterator(); iter.hasNext();)
		{
			Head_strs head_val = (Head_strs) iter.next();
			
			Vector<Vector<citation_view_vector>> c_view = citation_view_map2.get(head_val);
			
			row_num++;
			
			for(int p = 0; p<c_view.size(); p++)
			{
				origin_citation_size += c_view.get(p).size();
			}
			
		}
		
		
		
		if(row_num !=0)
			origin_citation_size = origin_citation_size / row_num;
		
		System.out.print(row_num + "	");
		
		System.out.print(origin_citation_size + "	");
		
		citation_view_map2.clear();
		
		citation_strs2.clear();
		
		citation_view2.clear();
		
		Tuple_reasoning1.compare(citation_view_map1, citation_view_map2);
		
//		Tuple_reasoning1.compare_citation(citation_strs, citation_strs2);
		
		System.out.println();
		
		Vector<String> unique_relation_names = get_relation_names(query);
		
		for(; view_size <= query.body.size(); )
		{				
			Vector<Query> curr_views = view_generator.gen_views(unique_relation_names, 5, view_size, view_id, query);view_generator.gen_views_with_n_subgoals(unique_relation_names, view_size, view_id);
			
			int m = 0;
			
			for(m = 0; m<curr_views.size(); m++)
			{
				view_generator.store_single_view(curr_views.get(m), view_id);
				
				System.out.println("start");
				
				System.out.println(curr_views.get(m));
				
				end_time = 0;

				
				start_time = System.nanoTime();
				
				
				for(int k = 0; k<times; k++)
				{
					
					citation_view_map1.clear();
					
					citation_strs.clear();
					
					Tuple_reasoning1_test.tuple_reasoning(query, citation_strs,  citation_view_map1, c, pst);
					
					System.gc();
				}
				
				
				
				end_time = System.nanoTime();
				
				time = (end_time - start_time);
				
				time = time /(times * 1.0 *1000000000);
				
				System.out.print(time + "s	");
				
				h_l = citation_strs.keySet();
				
				citation_size1 = 0;
				
				row = 0;
				
				for(Iterator iter = h_l.iterator(); iter.hasNext();)
				{
					Head_strs h_value = (Head_strs) iter.next();
					
					HashSet<String> citations = citation_strs.get(h_value);
					
					citation_size1 += citations.size();
					
					row ++;
					
				}
				
				if(row !=0)
				citation_size1 = citation_size1 / row;
				
				System.out.print(citation_size1 + "	");
				
				head = citation_view_map1.keySet();
				
				row_num = 0;
				
				origin_citation_size = 0.0;
				
				for(Iterator iter = head.iterator(); iter.hasNext();)
				{
					Head_strs head_val = (Head_strs) iter.next();
					
					Vector<Vector<citation_view_vector>> c_view = citation_view_map1.get(head_val);
					
					row_num++;
					
					for(int p = 0; p<c_view.size(); p++)
					{
						origin_citation_size += c_view.get(p).size();
					}
					
				}
				
				
				
				if(row_num !=0)
					origin_citation_size = origin_citation_size / row_num;
				
				System.out.print(row_num + "	");
				
				System.out.print(origin_citation_size + "	");
				
				
				citation_view_map1.clear();
				
				citation_strs.clear();
				
				citation_view1.clear();
				
				
				
				
				
				start_time = System.nanoTime();
				
				for(int k = 0; k<times; k++)
				{
					citation_view_map2.clear();
					
					citation_strs2.clear();
					
					citation_view2.clear();
					
					citation_view2 = Tuple_reasoning2_test.tuple_reasoning(query, citation_strs2, citation_view_map2, c, pst);
					
					System.gc();
				}
				
				end_time = System.nanoTime();
				
				time = (end_time - start_time);
				
				
				time = time /(times * 1.0 *1000000000);
				
				System.out.print(time + "s	");
				
				
				h_l = citation_strs2.keySet();
				
				citation_size2 = 0.0;
				
				row = 0;
				
				for(Iterator iter = h_l.iterator(); iter.hasNext();)
				{
					Head_strs h_value = (Head_strs) iter.next();
					
					HashSet<String> citations = citation_strs2.get(h_value);
					
					citation_size2 += citations.size();
					
					row ++;
					
				}
				
				if(row !=0)
					citation_size2 = citation_size2 / row;

				System.out.print(citation_size2 + "	");

				
				head = citation_view_map2.keySet();
				
				row_num = 0;
				
				origin_citation_size = 0.0;
				
				for(Iterator iter = head.iterator(); iter.hasNext();)
				{
					Head_strs head_val = (Head_strs) iter.next();
					
					Vector<Vector<citation_view_vector>> c_view = citation_view_map2.get(head_val);
					
					row_num++;
					
					for(int p = 0; p<c_view.size(); p++)
					{
						origin_citation_size += c_view.get(p).size();
					}
					
				}
				
				
				
				if(row_num !=0)
					origin_citation_size = origin_citation_size / row_num;
				
				System.out.print(row_num + "	");
				
				System.out.print(origin_citation_size + "	");
				
				citation_view_map2.clear();
				
				citation_strs2.clear();
				
				citation_view2.clear();
				
				Tuple_reasoning1.compare(citation_view_map1, citation_view_map2);
				
//				Tuple_reasoning1.compare_citation(citation_strs, citation_strs2);
				
				System.out.println();
				
				view_id ++;
				
				if(view_id >= upper_bound)
					break;
			}
			
			if(m < curr_views.size())
				break;
			
			
			view_size ++;
		}
	}
	
	static void reset(Connection c, PreparedStatement pst) throws SQLException, ClassNotFoundException
	{
		
		String query = "delete from user_query2conditions";
		
		pst = c.prepareStatement(query);
		
		pst.execute();
		
		query = "delete from user_query2subgoals";
		
		pst = c.prepareStatement(query);
		
		pst.execute();
		
		query = "delete from user_query_conditions";
		
		pst = c.prepareStatement(query);
		
		pst.execute();
		
		query = "delete from user_query_table";
		
		pst = c.prepareStatement(query);
		
		pst.execute();
		
		
	}

}
