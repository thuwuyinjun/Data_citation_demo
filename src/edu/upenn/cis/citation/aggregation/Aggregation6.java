package edu.upenn.cis.citation.aggregation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.jena.sparql.resultset.ResultSetMem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.upenn.cis.citation.Corecover.Lambda_term;
import edu.upenn.cis.citation.Corecover.Query;
import edu.upenn.cis.citation.Corecover.Tuple;
import edu.upenn.cis.citation.Operation.Conditions;
import edu.upenn.cis.citation.Pre_processing.populate_db;
import edu.upenn.cis.citation.citation_view.Head_strs;
import edu.upenn.cis.citation.citation_view.citation_view;
import edu.upenn.cis.citation.citation_view.citation_view_parametered;
import edu.upenn.cis.citation.citation_view.citation_view_unparametered;
import edu.upenn.cis.citation.citation_view.Covering_set;
import edu.upenn.cis.citation.data_structure.IntList;
import edu.upenn.cis.citation.data_structure.StringList;
import edu.upenn.cis.citation.data_structure.Unique_StringList;
import edu.upenn.cis.citation.datalog.Query_converter;
import edu.upenn.cis.citation.gen_citation.gen_citation0;
import edu.upenn.cis.citation.reasoning1.Tuple_level_approach;
import edu.upenn.cis.citation.reasoning1.Semi_schema_level_approach;
import edu.upenn.cis.citation.reasoning1.Schema_level_approach;

public class Aggregation6 {
	
	public static HashSet<HashMap<String, HashSet<String>>> full_citations = new HashSet<HashMap<String, HashSet<String>>>();
	
	public static HashMap<String, HashMap<String, HashSet<String>>> view_author_mapping = new HashMap<String, HashMap<String, HashSet<String>>>();
	
	public static HashMap<Tuple, ArrayList<Head_strs>> lambda_values = new HashMap<Tuple, ArrayList<Head_strs>>();
	
	public static void clear()
	{
	  full_citations.clear();
	  
	  view_author_mapping.clear();
	  
	  lambda_values.clear();
	}
	
//	public static void do_aggregate(ArrayList<citation_view_vector> curr_res, ArrayList<citation_view_vector> c_views, int seq, ArrayList<HashMap<String, HashSet<String>> > author_list, ArrayList<HashMap<String, Integer>> view_query_mapping, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, ArrayList<Lambda_term[]> query_lambda_str, StringList view_list, HashMap<String, Boolean> full_flag, Connection c, PreparedStatement pst) throws ClassNotFoundException, SQLException
//	{
//		
//		if(seq == 0)
//		{
//			
//			curr_res.addAll(c_views);
//			
//			for(int i = 0; i<c_views.size(); i++)
//			{
//				HashMap<String, HashSet<String>> json_citation = gen_citation1.join_covering_sets(c_views.get(i), c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
//				
//				author_list.add(json_citation);
//				
//			}
//			
//			
//			return;
//			
//		}
//				
//		int i1 = 0;
//		
//		int i2 = 0;
//		
//		for(int i = 0; i<curr_res.size(); i++)
//		{
//			
//			String id1 = curr_res.get(i).view_name_str;
//			
//			int j = 0;
//			
//			citation_view_vector c_vector = null;
//			
//			for(j = 0; j<c_views.size(); j++)
//			{
//				String id2 = c_views.get(j).view_name_str;
//				
//				c_vector = c_views.get(j);
//				
//				if(id1.equals(id2))
//				{
//					break;
//				}
//			}
//			
//			if(j < c_views.size())
//			{				
//							
//				HashMap<String, HashSet<String>> curr_authors = author_list.get(i);
//				
//				if(check_block_full(curr_authors, max_num, full_flag))
//					continue;
//				
//				HashMap<String, HashSet<String>> authors = gen_citation1.join_covering_sets(c_vector, c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);				
//				
////				System.out.println(c_vector + ":::::::::::" + authors);
//
//				
//				Set<String> keys = authors.keySet();
//				
//				for(Iterator iter = keys.iterator(); iter.hasNext();)
//				{
//					String key = (String) iter.next();
//					
//					HashSet<String> values = authors.get(key);
//					
//					if(curr_authors.get(key) == null)
//					{
//						curr_authors.put(key, values);
//					}
//					else
//					{
//						
//						curr_authors.get(key).addAll(values);
//					}
//				}
//				
//				author_list.set(i, curr_authors);
//				
////				for(int k = 0; k<c_vector.c_vec.size(); k++)
////				{
////					citation_view c_v = c_vector.c_vec.get(k);
////					
////					
////					int view_index = view_list.find(c_vector.c_vec.get(i).get_name());
////					
////					HashMap<String, HashSet<String>> authors = null;
////					
////					if(c_v.has_lambda_term())
////					{
////						authors = gen_citation1.get_authors3((citation_view_parametered)c_v, c, pst, view_index, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
////						
////						author_list.get(i).addAll(authors);
////					}
////					else
////					{
////						authors = gen_citation1.get_authors3((citation_view_unparametered)c_v, c, pst, view_index, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
////						
////						author_list.get(i).addAll(authors);
////					}
////					
////					for(Iterator iter = authors.iterator(); iter.hasNext() && author_list.get(i).size() <= max_num;)
////					{
////						author_list.get(i).add((String) iter.next());
////					}
////				}
//				
//				
//			}
//			else
//			{
//			
//				curr_res.remove(i);
//				
//				author_list.remove(i);
//				
//				i--;
//			}
//		}
//		
//	}
	
	public static void do_aggregate(ArrayList<Covering_set> curr_res, ArrayList<Covering_set> c_views, int seq) throws ClassNotFoundException, SQLException
	{
		
	
		if(seq == 0)
		{
			
			curr_res.addAll(c_views);
			
			return;
			
		}
		
		for(int i = 0; i<curr_res.size(); i++)
		{
			
			String id1 = curr_res.get(i).view_name_str;
			
			String t_id1 = curr_res.get(i).table_name_str;
			
			int j = 0;
						
			for(j = 0; j<c_views.size(); j++)
			{
				String id2 = c_views.get(j).view_name_str;
				
				String t_id2 = c_views.get(j).table_name_str;
								
				if(id1.equals(id2) && t_id1.equals(t_id2))
				{
					break;
				}
			}
			
			if(j >= c_views.size())
			{				
					
				curr_res.remove(i);
				
				i--;			
			}
		}
		
	}
	
	public static void do_aggregate(ArrayList<Covering_set> curr_res, HashSet<Covering_set> c_views, int seq)
	{
		
	
		if(seq == 0)
		{
			
			curr_res.addAll(c_views);
			
			return;
			
		}
		
	      for(int i = 0; i<curr_res.size(); i++)
	        {
	            
	          Covering_set covering_set1 = curr_res.get(i);
	          
	            int j = 0;
	                        
	            for(Iterator iter = c_views.iterator(); iter.hasNext();)
	            {
	                
	                Covering_set covering_set = (Covering_set) iter.next();
	                
	                if(covering_set.equals(covering_set1))
	                {
	                    break;
	                }
	                
	                j++;
	            }
	            
	            if(j >= c_views.size())
	            {               
	                    
	                curr_res.remove(i);
	                
	                i--;            
	            }
	        }
		
	}
	
//	public static void do_aggregate(citation_view_vector curr_res, citation_view_vector c_views, int seq, ArrayList<HashMap<String, HashSet<String>> > author_list, ArrayList<HashMap<String, Integer>> view_query_mapping, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, ArrayList<Lambda_term[]> query_lambda_str, StringList view_list, HashMap<String, Boolean> full_flag, HashMap<String, String> view_citation_mapping, boolean tuple_level, Connection c, PreparedStatement pst) throws ClassNotFoundException, SQLException
//	{
//		
//	
//		if(seq == 0)
//		{			
////			for(int i = 0; i<c_views.size(); i++)
//			{
//				HashMap<String, HashSet<String>> json_citation = gen_citation1.join_covering_sets(c_views, c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
//				
//				author_list.add(json_citation);
//				
//				view_citation_mapping.put(c_views.toString(), json_citation.toString());
//				
//				curr_res
//				
//			}
//			
//			
//			return;
//			
//		}
//				
//		int i1 = 0;
//		
//		int i2 = 0;
//		
//		for(int i = 0; i<curr_res.size(); i++)
//		{
//			
//			String id1 = curr_res.get(i).view_name_str;
//			
//			String t_id1 = curr_res.get(i).table_name_str;
//			
//			int j = 0;
//			
//			citation_view_vector c_vector = null;
//			
//			for(j = 0; j<c_views.size(); j++)
//			{
//				String id2 = c_views.get(j).view_name_str;
//				
//				String t_id2 = c_views.get(j).table_name_str;
//				
//				c_vector = c_views.get(j);
//				
//				if(id1.equals(id2) && t_id1.equals(t_id2))
//				{
//					break;
//				}
//			}
//			
//			if(j < c_views.size())
//			{				
//							
//				HashMap<String, HashSet<String>> curr_authors = author_list.get(i);
//				
//				if(check_block_full(curr_authors, max_num, full_flag))
//					continue;
//				
//				HashMap<String, HashSet<String>> authors = gen_citation1.join_covering_sets(c_vector, c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);				
//
//				view_citation_mapping.put(c_vector.toString(), authors.toString());
//
//				
//				Set<String> keys = authors.keySet();
//				
//				for(Iterator iter = keys.iterator(); iter.hasNext();)
//				{
//					String key = (String) iter.next();
//					
//					HashSet<String> values = authors.get(key);
//					
//					if(curr_authors.get(key) == null)
//					{
//						curr_authors.put(key, values);
//					}
//					else
//					{
//						
//						curr_authors.get(key).addAll(values);
//					}
//				}
//				
//				author_list.set(i, curr_authors);
//				
//			}
//			else
//			{
//			
//				curr_res.remove(i);
//				
//				author_list.remove(i);
//				
//				i--;
//			}
//		}
//		
//	}
//	
	static boolean check_block_full(HashMap<String, HashSet<String>> curr_authors, HashMap<String, Integer> max_num, HashMap<String, Boolean> full_flag)
	{
		Set<String> keys = curr_authors.keySet();
		
		boolean full_filled = true;
		
		for(Iterator iter = keys.iterator(); iter.hasNext();)
		{
			String key = (String) iter.next();
			
			if(full_flag.get(key))
			{				
				continue;
			}
			
			if(max_num.get(key) == null)
			{
				full_filled = full_filled & false;
				
				continue;
			}
			
			int curr_max_num = max_num.get(key);
			
			if(curr_max_num <= 0)
			{
				full_filled = full_filled & false;
				
				continue;
			}
			
			HashSet<String> curr_values = curr_authors.get(key);
			
			if(curr_max_num > 0 && curr_values.size() > curr_max_num)
			{
				
				full_filled = full_filled & true;
				
				full_flag.put(key, true);
			}
			else
			{
				full_filled = full_filled & false;
			}
		}
		
		return full_filled;
	}
	
	public static ArrayList<Integer> get_all_selected_row_ids(ArrayList<Head_strs> heads, HashMap<Head_strs, ArrayList<Integer>> head_strs_rows_mapping)
	{
		ArrayList<Integer> all_row_ids = new ArrayList<Integer>();
		
		for(int i = 0; i<heads.size(); i++)
		{
			all_row_ids.addAll(head_strs_rows_mapping.get(heads.get(i)));
		}
		
		
		
		Collections.sort(all_row_ids);
		
		return all_row_ids;
	}
	
	public static HashSet<String> do_agg_intersection(ResultSet rs, HashMap<int[], ArrayList<Covering_set> > c_view_map, int start_pos, ArrayList<Head_strs> heads, HashMap<Head_strs, ArrayList<Integer>> head_strs_rows_mapping, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, ClassNotFoundException, JSONException
	{
		
		HashMap<String, Boolean> full_flag = new HashMap<String, Boolean>();
		
		for(int i = 0; i<populate_db.available_block.length; i++)
		{
			full_flag.put(populate_db.available_block[i], false);
		}
		
		ArrayList<Integer> all_row_ids = get_all_selected_row_ids(heads, head_strs_rows_mapping);
		
		Set<int[]> intervals = c_view_map.keySet();
		
		int index = 0;
		
		rs.beforeFirst();
		
		HashSet<HashMap<String, HashSet<String>>> author_lists = new HashSet<HashMap<String, HashSet<String>>>();
		
		ArrayList<Covering_set> curr_res = new ArrayList<Covering_set>();
				
		for(Iterator iter = intervals.iterator(); iter.hasNext();)
		{
			int [] interval = (int[]) iter.next();
			
			ArrayList<Covering_set> c_view = c_view_map.get(interval);
			
			for(int i = index; i < all_row_ids.size(); i ++)
			{				
				if(all_row_ids.get(i) < interval[1] && all_row_ids.get(i) >= interval[0])
				{
//					rs.absolute(all_row_ids.get(i) + 1);
					
					
//					if(tuple_level)
//						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
//					else
//						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
						
					do_aggregate(curr_res, c_view, i);
					
//					do_aggregate(curr_res, c_view, i, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, c, pst);
				}
				
				
				if(all_row_ids.get(index) >= interval[1])
				{
					index = i;
					
					break;
				}
			}
		}
		
		
		return gen_citations(author_lists, max_num);
		
	}
	
	public static HashSet<String> gen_citations(HashSet<HashMap<String, HashSet<String>>> author_lists, HashMap<String, Integer> max_num)
	{
		HashSet<String> json_string = new HashSet<String>();
			
		for(HashMap<String, HashSet<String>> authors: author_lists)
		{
//			boolean empty = false;
			
			JSONObject json = gen_citation0.get_json_citation(authors);
			
			if(json == null)
				continue;
		
			json_string.add(json.toString());
		}
		
		return json_string;
	}
	
//	public static Vector<String> do_agg_intersection(Vector<Head_strs> heads, HashMap<Head_strs, ArrayList<ArrayList<citation_view_vector>>> heads_citation_views, ArrayList<HashMap<String, Integer>> view_query_mapping, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, HashMap<String, HashSet<String>>> view_author_mapping, Connection c, PreparedStatement pst) throws ClassNotFoundException, SQLException
//	{
//		
//		ArrayList<citation_view_vector> curr_res = new ArrayList<citation_view_vector>();
//		
//		Vector<HashSet<String>> author_lists = new Vector<HashSet<String>>();
//		
//		int seq = 0;
//		
//		for(int i = 0; i<heads.size(); i++)
//		{
//			ArrayList<ArrayList<citation_view_vector>> citation_views = heads_citation_views.get(heads.get(i));
//			
//			for(int j = 0; j<citation_views.size(); j++)
//			{
//				do_aggregate(curr_res, citation_views.get(j), seq, author_lists, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping, , c, pst);
//				
//				seq ++;
//			}
//			
//		}
//		
//		return gen_citations(author_lists, max_num);
//	}
//	
	
	//query_ids, view_list, 
//	public static HashSet<String> do_agg_intersection(ResultSet rs, HashMap<int[], ArrayList<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, ClassNotFoundException, JSONException
//	{
//		
//		HashMap<String, Boolean> full_flag = new HashMap<String, Boolean>();
//		
//		for(int i = 0; i<populate_db.available_block.length; i++)
//		{
//			full_flag.put(populate_db.available_block[i], false);
//		}
//		
//		Set<int[]> intervals = c_view_map.keySet();
//				
//		rs.beforeFirst();
//		
////		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		int i = 0;
//						
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			int [] interval = (int[]) iter.next();
//			
//			ArrayList<citation_view_vector> c_view = c_view_map.get(interval);
//			
////			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
////					rs.absolute(i + 1);
//					
////					if(tuple_level)
////						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
////					else
////						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
//					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//			
//			i++;
//		}
//		
//		ArrayList<String> view_keys = new ArrayList<String>();
//		
//		ArrayList<citation_view> single_views = get_single_citation_views(curr_res, view_keys);
//		
//		ArrayList<String> single_view_names = get_citation_view_names(single_views);
//						
//		int tuple_num = (tuple_level) ? Tuple_reasoning1_full_test_opt_copy.tuple_num : Tuple_reasoning2_full_test2_copy.tuple_num;
//		
//		for(i = 0; i<tuple_num; i++)
//		{
//			rs.absolute(i + 1);
//			
//			if(tuple_level)
//				Tuple_reasoning1_full_test_opt_copy.get_views_parameters(single_views, rs, start_pos, lambda_values);
//			else
//				Tuple_reasoning2_full_test2_copy.get_views_parameters(single_views, rs, start_pos, lambda_values);
//			
////			convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
//			
//			
//		}
//		
//		
//		
//		ArrayList<HashMap<String, HashSet<String>>> citations = gen_citation_view_level(single_views, lambda_values, tuple_level, false, c, pst);
//				
//		full_citations = gen_citations_covering_set_level(citations, curr_res, single_view_names, view_keys);
//		
//		return gen_citations(full_citations, max_num);
//		
////		return new HashSet<String>();
//	}
	
//	public static HashSet<String> do_agg_intersection2(HashSet<Covering_set> covering_set_schema_level, HashSet<Tuple> valid_view_mappings_schema_level, ResultSet rs, HashMap<String, HashSet<Covering_set> > c_view_map, int start_pos, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, ArrayList<ArrayList<String>>> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, Connection c, PreparedStatement pst, boolean tuple_level, HashMap<String, HashMap<String, String>> view_citation_query_mappings, HashMap<String, Query> citation_query_name_mappings) throws SQLException, JSONException
//	{
//		
//		
//		
//		
//		return gen_citation_entire_query(covering_set_schema_level, rs, tuple_level, false, valid_view_mappings_schema_level, start_pos, max_num, c, pst,view_citation_query_mappings, citation_query_name_mappings);
////		return new HashSet<String>();
//	}
	
//	public static HashSet<String> do_agg_intersection_subset(ResultSet rs, HashSet<Integer> tuple_ids, HashMap<String, HashSet<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, JSONException
//    {
//        
//        
//        
//        
//        return gen_citation_subset(rs, tuple_ids, tuple_level, false, curr_res, start_pos, max_num, c, pst);
////      return new HashSet<String>();
//    }
	
//	public static HashSet<String> do_agg_intersection0(ResultSet rs, HashMap<String, HashSet<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, ClassNotFoundException, JSONException
//	{
//		
//		Set<String> intervals = c_view_map.keySet();
//		
//		rs.beforeFirst();
//		
////		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		int i = 0;
//						
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			String interval = (String) iter.next();
//			
//			HashSet<citation_view_vector> c_view = c_view_map.get(interval);
//			
////			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
////					rs.absolute(i + 1);
//					
////					if(tuple_level)
////						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
////					else
////						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
//					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//			
//			i++;
//		}
//		
//		
//		return gen_citation_entire_query(rs, tuple_level, false, curr_res, start_pos, max_num, c, pst);
////		return new HashSet<String>();
//	}
	
	static ArrayList<citation_view> get_single_citation_views(HashSet<Tuple> valid_view_mappings_schema_level)
	{
	  ArrayList<citation_view> citation_views = new ArrayList<citation_view>();
	  
	  for(Tuple valid_tuple: valid_view_mappings_schema_level)
	  {
	    
	    
	    if(valid_tuple.lambda_terms.size() > 0)
        {
            
            citation_view_parametered c = new citation_view_parametered(valid_tuple.name, valid_tuple.query, valid_tuple);
            
            citation_views.add(c);
        }   
        else
        {
            
            citation_view_unparametered c = new citation_view_unparametered(valid_tuple.name, valid_tuple);
            
            citation_views.add(c);
            
        }
	  }
	  
	  return citation_views;
	}
	
	public static HashSet<String> gen_citation_entire_query(HashSet<Covering_set> covering_set_schema_level, ResultSet rs, int tuple_num, HashSet<Tuple> valid_view_mappings_schema_level, int start_pos, HashMap<String, Integer> max_num, Connection c, PreparedStatement pst, HashMap<String, HashMap<String, String>> view_citation_query_mappings, HashMap<String, Query> citation_query_name_mappings, HashMap<String, Integer> lambda_term_id_mapping) throws SQLException
	{
		ArrayList<String> view_keys = new ArrayList<String>();
		
//		ArrayList<citation_view> single_views = get_single_citation_views(valid_view_mappings_schema_level);
		
//		ArrayList<String> single_view_names = get_citation_view_names(single_views);
						
//		int tuple_num = 0;
//		
//		if(tuple_level)
//		{
//			tuple_num = Tuple_level_approach.tuple_num;
//		}
//		else
//		{
//			if(!schema_level)
//			{
//				tuple_num = Semi_schema_level_approach.tuple_num;
//			}
//			else
//			{
//				tuple_num = Schema_level_approach.tuple_num;
//			}
//		}
		
		for(int i = 0; i<tuple_num; i++)
		{
			rs.absolute(i + 1);
			
//			if(tuple_level)
//				Tuple_level_approach.get_views_parameters(valid_view_mappings_schema_level, rs, start_pos, lambda_values);
//			else
//			{
//				if(!schema_level)
//					Semi_schema_level_approach.get_views_parameters(valid_view_mappings_schema_level, rs, start_pos, lambda_values);
//				else
					get_views_parameters(valid_view_mappings_schema_level, rs, start_pos, lambda_values, lambda_term_id_mapping);
//			}
			
//			convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
			
			
		}
		
		for(Tuple view_mapping: valid_view_mappings_schema_level)
		{
		  if(view_mapping.lambda_terms.isEmpty())
		  {
		    lambda_values.put(view_mapping, null);
		  }
		}
		
		
		
		HashMap<Tuple, HashMap<String, HashSet<String>>> citations = gen_citation_view_level(valid_view_mappings_schema_level, lambda_values, view_citation_query_mappings, citation_query_name_mappings, c, pst);
				
		full_citations = gen_citations_covering_set_level(citations, covering_set_schema_level, valid_view_mappings_schema_level);
		
		return gen_citations(full_citations, max_num);
	}

//	public static HashSet<String> gen_citation_subset(ResultSet rs, HashSet<Integer> id_list, boolean tuple_level, boolean schema_level, ArrayList<citation_view_vector> covering_sets, int start_pos, HashMap<String, Integer> max_num, Connection c, PreparedStatement pst) throws SQLException
//    {
//        ArrayList<String> view_keys = new ArrayList<String>();
//        
//        ArrayList<citation_view> single_views = get_single_citation_views(covering_sets, view_keys);
//        
//        ArrayList<String> single_view_names = get_citation_view_names(single_views);
//                        
////        int tuple_num = 0;
////        
////        if(tuple_level)
////        {
////            tuple_num = Tuple_reasoning1_full_test_opt.tuple_num;
////        }
////        else
////        {
////            if(!schema_level)
////            {
////                tuple_num = Tuple_reasoning2_full_test2.tuple_num;
////            }
////            else
////            {
////                tuple_num = schema_reasoning.tuple_num;
////            }
////        }
//        
//        for(Integer id : id_list)
//        {
//            rs.absolute(id);
//            
//            if(tuple_level)
//                Tuple_reasoning1_full_test_opt_copy.get_views_parameters(single_views, rs, start_pos, lambda_values);
//            else
//            {
//                if(!schema_level)
//                    Tuple_reasoning2_full_test2_copy.get_views_parameters(single_views, rs, start_pos, lambda_values);
//                else
//                    schema_reasoning.get_views_parameters(single_views, rs, start_pos, lambda_values);
//            }
//            
////          convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
//            
//            
//        }
//        
//        
//        
//        ArrayList<HashMap<String, HashSet<String>>> citations = gen_citation_view_level(single_views, lambda_values, tuple_level, schema_level, c, pst);
//                
//        full_citations = gen_citations_covering_set_level(citations, covering_sets, single_view_names, view_keys);
//        
//        return gen_citations(full_citations, max_num);
//    }
	
	public static HashSet<Integer> get_valid_tuple_ids(HashMap<Head_strs, ArrayList<Integer>> head_strs_rows_mapping, Vector<Head_strs> names)
	{
	  
	  HashSet<Integer> valid_id_lists = new HashSet<Integer>();
	  
	  for(int i = 0; i<names.size(); i++)
	  {
//	    Head_strs head_str = new Head_strs(names.get(i));
	    
	    ArrayList<Integer> id_lists = head_strs_rows_mapping.get(names.get(i));
	    
	    valid_id_lists.addAll(id_lists);
	  }
	  
//	  Set.sort(valid_id_lists, new IntegerComparator());
	  
	  return valid_id_lists;
	  
	}
	
	static class IntegerComparator implements Comparator<Integer> {
      public int compare(Integer o1, Integer o2) {
          int result = o1 - o2;

          if (result == 0) {
              return (o1 < o2) ? -1 : 1;
          } else {
              return result;
          }
      }
  }
	
//	public static void cal_covering_set_schema_level_intersection(ResultSet rs, HashSet<Integer> tuple_ids, HashMap<String, HashSet<Covering_set> > c_view_map, HashMap<String, HashSet<Integer>> signature_rid_mappings, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException
//    {
//	  
//        Set<String> intervals = c_view_map.keySet();
//        
//        rs.beforeFirst();
//        
////      ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//        
//        int i = 0;
//            
//        Iterator iter = intervals.iterator();
//        
//        String interval = null;
//        
//        int group_num = 0;
//        
//        while(iter.hasNext())
//        {
//          interval = (String) iter.next();
//          
//          HashSet<Integer> rids = (HashSet<Integer>) signature_rid_mappings.get(interval).clone();
//          
//          rids.retainAll(tuple_ids);
//          
//          if(!rids.isEmpty())
//          {
//            HashSet<Covering_set> c_view = c_view_map.get(interval);
//            
//            do_aggregate(curr_res, c_view, group_num);
//            
//            group_num ++;
//          }
//          
////          boolean match = false;
////          
////          while(tuple_ids.get(i) >= interval[0] && tuple_ids.get(i) < interval[1])
////          {
////            match = true;
////            
////            i++;
////            
////            if(i >= tuple_ids.size())
////              break;
////          }
////          
////          if(match)
////          {
////            
////          }
////          
////          if(i >= tuple_ids.size())
////            break;
//        }
//          
//    }

	
//	public static void cal_covering_set_schema_level_intersection(ResultSet rs, HashMap<String, HashSet<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException
//	{
//		Set<String> intervals = c_view_map.keySet();
//		
//		rs.beforeFirst();
//		
////		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		int i = 0;
//						
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			String interval = (String) iter.next();
//			
//			HashSet<citation_view_vector> c_view = c_view_map.get(interval);
//			
////			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
////					rs.absolute(i + 1);
//					
////					if(tuple_level)
////						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
////					else
////						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
//					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//			
//			i++;
//		}
//	}

	
	public static int[] count_view_mapping_predicates_lambda_terms(HashSet<Tuple> tuples)
	{
		
		HashSet<String> view_mapping_string = new HashSet<String>();
		
		HashSet<String> lambda_string = new HashSet<String> ();
		
		HashSet<String> predicate_string = new HashSet<String>();
		
		for(Iterator iter = tuples.iterator(); iter.hasNext();)
		{
			Tuple tuple = (Tuple) iter.next();
			
			Vector<Conditions> conditions = tuple.conditions;
			
			Vector<Lambda_term> l_terms = tuple.lambda_terms;
			
			for(int k = 0; k<conditions.size(); k++)
			{
				predicate_string.add(conditions.get(k).toString());
			}
			
			for(int k = 0; k<l_terms.size(); k++)
			{
				lambda_string.add(l_terms.get(k).toString());
			}
		}
		
//		for(int i = 0; i<curr_res.size(); i++)
//		{
//			citation_view_vector covering_set = curr_res.get(i);
//			
//			for(int j = 0; j<covering_set.c_vec.size(); j++)
//			{
//				citation_view v = covering_set.c_vec.get(j);
//				
//				String view_name = v.get_name();
//				
//				String table_names = v.get_table_name_string();
//				
//				String index_string = view_name + populate_db.separator + table_names;
//				
//				
//				Vector<Conditions> conditions = v.get_view_tuple().conditions;
//				
//				Vector<Lambda_term> l_terms = v.get_view_tuple().lambda_terms;
//				
//				for(int k = 0; k<conditions.size(); k++)
//				{
//					predicate_string.add(conditions.get(k).toString());
//				}
//				
//				for(int k = 0; k<l_terms.size(); k++)
//				{
//					lambda_string.add(l_terms.get(k).toString());
//				}
//				
////				if(view_mapping_string.contains(index_string))
//				{
//					view_mapping_string.add(index_string);
//				}
//			}
//		}
		
		int [] nums = new int[3];
		
		nums[0] = tuples.size();
		
		nums[1] = lambda_string.size();
		
		nums[2] = predicate_string.size();
		
		return nums;
	}
	
//	public static ArrayList<HashSet<citation_view>> cal_covering_set_schema_level_union(ResultSet rs, HashMap<String, HashSet<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException
//	{
//		Set<String> intervals = c_view_map.keySet();
//		
//		rs.beforeFirst();
//		
////		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		int i = 0;
//		
//		int tuple_num = (tuple_level) ? Tuple_reasoning1_full_test_opt_copy.tuple_num : Tuple_reasoning2_full_test2_copy.tuple_num;
//
//		int group_num = (tuple_level) ? Tuple_reasoning1_full_test_opt_copy.group_num : Tuple_reasoning2_full_test2_copy.group_num;
//		
//		HashSet<citation_view_vector> all_covering_sets = new HashSet<citation_view_vector>();
//		
//						
//		ArrayList<HashSet<citation_view>> views_per_group = new ArrayList<HashSet<citation_view>>();
//		
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			String interval = (String) iter.next();
//			
//			HashSet<citation_view_vector> c_view = c_view_map.get(interval);
//			
//			HashSet<citation_view> views = new HashSet<citation_view>();
//			
//			for(Iterator it = c_view.iterator(); it.hasNext();)
//			{
//				citation_view_vector view = (citation_view_vector) it.next();
//				
//				views.addAll(view.c_vec);
//			}
//			
//			views_per_group.add(views);
//			
//			all_covering_sets.addAll(c_view);
////			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
////					rs.absolute(i + 1);
//					
////					if(tuple_level)
////						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
////					else
////						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
////					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//		}
//		
//		curr_res.addAll(all_covering_sets);
//		
//		return views_per_group;
//	}
	
	public static void get_views_parameters(HashSet<Tuple> insert_c_view, ResultSet rs, int start_pos, HashMap<Tuple, ArrayList<Head_strs>> values, HashMap<String, Integer> lambda_term_id_mapping) throws SQLException
    {
        
//      HashSet<citation_view_vector2> update_c_views = new HashSet<citation_view_vector2>();
        
        Set<String> head_sets = lambda_term_id_mapping.keySet();
        
        for(Tuple view_mapping: insert_c_view)
        {
            
//          citation_view c_vec = insert_c_view.get(i);
            
//          for(int j = 0; j<c_vec.c_vec.size(); j++)
//          {
//              citation_view c_view = c_vec.c_vec.get(j);
                
                if(view_mapping.lambda_terms.size() > 0)
                {
//                  citation_view_parametered curr_c_view = (citation_view_parametered) c_vec;
                    
//                  Vector<Lambda_term> lambda_terms = curr_c_view.lambda_terms;
                    
                    Vector<String> heads = new Vector<String>();
                    
                    for(int k = 0; k<view_mapping.lambda_terms.size(); k++)
                    {
                        int id = lambda_term_id_mapping.get(view_mapping.lambda_terms.get(k).toString());
                        
                        heads.add(rs.getString(id + start_pos + 1));
                        
//                      curr_c_view.put_lambda_paras(lambda_terms.get(k), rs.getString(id + start_pos + 1));
                    }
                    
                    Head_strs head_values = new Head_strs(heads);
                    
                    if(values.get(view_mapping) == null)
                    {
                      
                      ArrayList<Head_strs> curr_head_values = new ArrayList<Head_strs>();
                      
                      curr_head_values.add(head_values);
                      
                        values.put(view_mapping, curr_head_values);
                    }
                    else
                    {
//                      HashSet<Head_strs> curr_head_values = new HashSet<Head_strs>();
//                      
//                      curr_head_values.add(head_values);
                        
                        values.get(view_mapping).add(head_values);//add(curr_head_values);
                    }
                    
                }
//              else
//              {
//                  if(values.size() > i)
//                  {
//                      continue;
//                  }
//                  else
//                  {
//                      values.add(new HashSet<Head_strs>());
//                  }
//              }
                
//          }
        }
    }
    
	
	
	public static HashSet<String> do_agg_union(HashSet<Covering_set> covering_set_schema_level, HashSet<Tuple> valid_view_mapping_schema_level,  HashMap<String, HashSet<Tuple>> signature_view_mappings_mappings, HashMap<String, HashSet<Integer>> signiture_rid_mappings, ResultSet rs, HashMap<String, HashSet<Integer>> signature_rids_mappings, int start_pos, HashMap<String, ArrayList<ArrayList<String>>> author_mapping, HashMap<String, Integer> max_num, HashMap<String, Integer> lambda_term_id_mapping, Connection c, PreparedStatement pst, HashMap<String, HashMap<String, String>> view_citation_query_mappings, HashMap<String, Query> citation_query_name_mappings, boolean tuple_level) throws SQLException, JSONException
	{
		
		Set<String> signature_set = signature_view_mappings_mappings.keySet();
		
		for(String curr_signature:signature_set)
		{
//		  HashSet<Tuple> curr_view_mappings = signature_view_mappings_mappings.get(curr_signature);
		  
		  HashSet<Integer> rids = signature_rids_mappings.get(curr_signature);
		  
		  for(Integer i: rids)
          {
              
//            if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
              {
                  rs.absolute(i);
                  
                  get_views_parameters(valid_view_mapping_schema_level, rs, start_pos, lambda_values, lambda_term_id_mapping);
//                  else
//                      Semi_schema_level_approach.get_views_parameters(valid_view_mapping_schema_level, rs, start_pos, lambda_values);
                  
//                do_aggregate(curr_res, c_view, i);
                  
//                System.out.println(curr_res);
              }
          }
		}
		
//		int i = 0;
		
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			String interval = (String) iter.next();
//						
//			HashSet<Integer> rids = signature_rids_mappings.get(interval);
//			
//			ArrayList<Integer> view_ids = get_valid_view_id(single_views, views_per_group.get(num));
//			
//			for(Integer i: rids)
//			{
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
//					rs.absolute(i + 1);
//					
//					if(tuple_level)
//						Tuple_reasoning1_full_test_opt_copy.get_views_parameters(single_views, rs, start_pos, lambda_values, view_ids);
//					else
//						Tuple_reasoning2_full_test2_copy.get_views_parameters(single_views, rs, start_pos, lambda_values, view_ids);
//					
////					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//			
//			num ++;
//			
//		}
		
		
//		for(i = 0; i<tuple_num; i++)
//		{
//			rs.absolute(i + 1);
//			
//			if(tuple_level)
//				Tuple_reasoning1_full_test_opt.get_views_parameters(single_views, rs, start_pos, lambda_values);
//			else
//				Tuple_reasoning2_full_test2.get_views_parameters(single_views, rs, start_pos, lambda_values);
//			
////			convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
//			
//			
//		}
		
		
		
		HashMap<Tuple, HashMap<String, HashSet<String>>> citations = gen_citation_view_level(valid_view_mapping_schema_level, lambda_values, view_citation_query_mappings, citation_query_name_mappings, c, pst);
				
		full_citations = gen_citations_covering_set_level(citations, covering_set_schema_level, valid_view_mapping_schema_level);
		
		return gen_citations(full_citations, max_num);
		
//		return new HashSet<String>();
	}
	
//	public static HashSet<String> do_agg_union0(ResultSet rs, HashMap<String, HashSet<citation_view_vector> > c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, ClassNotFoundException, JSONException
//	{
//		
//		Set<String> intervals = c_view_map.keySet();
//		
//		rs.beforeFirst();
//		
////		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		int i = 0;
//		
//		int tuple_num = (tuple_level) ? Tuple_reasoning1_full_test_opt_copy.tuple_num : Tuple_reasoning2_full_test2_copy.tuple_num;
//
//		int group_num = (tuple_level) ? Tuple_reasoning1_full_test_opt_copy.group_num : Tuple_reasoning2_full_test2_copy.group_num;
//		
//		HashSet<citation_view_vector> all_covering_sets = new HashSet<citation_view_vector>();
//		
//						
//		ArrayList<HashSet<citation_view>> views_per_group = new ArrayList<HashSet<citation_view>>();
//		
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			String interval = (String) iter.next();
//			
//			HashSet<citation_view_vector> c_view = c_view_map.get(interval);
//			
//			HashSet<citation_view> views = new HashSet<citation_view>();
//			
//			for(Iterator it = c_view.iterator(); it.hasNext();)
//			{
//				citation_view_vector view = (citation_view_vector) it.next();
//				
//				views.addAll(view.c_vec);
//			}
//			
//			views_per_group.add(views);
//			
//			all_covering_sets.addAll(c_view);
////			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
////					rs.absolute(i + 1);
//					
////					if(tuple_level)
////						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
////					else
////						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
////					do_aggregate(curr_res, c_view, i);
//					
////					System.out.println(curr_res);
//				}
//			}
//		}
//		
//		curr_res.addAll(all_covering_sets);
//		
//		ArrayList<String> view_keys = new ArrayList<String>();
//		
//		ArrayList<citation_view> single_views = get_single_citation_views(curr_res, view_keys);
//		
//		ArrayList<String> single_view_names = get_citation_view_names(single_views);
//				
//		initialize_view_lambda_sets(lambda_values, single_views.size());
//				
//		int num = 0;
//				
////		for(Iterator iter = intervals.iterator(); iter.hasNext();)
////		{
////			String interval = (String) iter.next();
////			
////			HashSet<citation_view_vector> curr_covering_sets = c_view_map.get(interval);
////			
////			
////						
////			ArrayList<Integer> view_ids = get_valid_view_id(single_views, views_per_group.get(num));
////			
////			for(i = interval[0]; i < interval[1]; i ++)
////			{
////				
//////				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
////				{
////					rs.absolute(i + 1);
////					
////					if(tuple_level)
////						Tuple_reasoning1_full_test_opt_copy.get_views_parameters(single_views, rs, start_pos, lambda_values, view_ids);
////					else
////						Tuple_reasoning2_full_test2_copy.get_views_parameters(single_views, rs, start_pos, lambda_values, view_ids);
////					
//////					do_aggregate(curr_res, c_view, i);
////					
//////					System.out.println(curr_res);
////				}
////			}
////			
////			num ++;
////			
////		}
//		
//		
////		for(i = 0; i<tuple_num; i++)
////		{
////			rs.absolute(i + 1);
////			
////			if(tuple_level)
////				Tuple_reasoning1_full_test_opt.get_views_parameters(single_views, rs, start_pos, lambda_values);
////			else
////				Tuple_reasoning2_full_test2.get_views_parameters(single_views, rs, start_pos, lambda_values);
////			
//////			convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
////			
////			
////		}
//		
//		
//		
//		ArrayList<HashMap<String, HashSet<String>>> citations = gen_citation_view_level(single_views, lambda_values, tuple_level, false, c, pst);
//				
//		full_citations = gen_citations_covering_set_level(citations, curr_res, single_view_names, view_keys);
//		
//		return gen_citations(full_citations, max_num);
//		
////		return new HashSet<String>();
//	}
//	
	public static int cal_distinct_views()
	{
		int size = 0;
		
		for(int i = 0; i < lambda_values.size(); i++)
		{
			if(lambda_values.get(i).size() == 0)
			{
				size += 1;
			}
			else
			{
				size += lambda_values.get(i).size();
			}
		}
		
		return size;
	}
	
	static void initialize_view_lambda_sets(ArrayList<HashSet<Head_strs>> lambda_values, int size)
	{
		for(int i = 0; i<size; i++)
		{
			lambda_values.add(new HashSet<Head_strs>());
		}
	}
	
	static ArrayList<Integer> get_valid_view_id(ArrayList<citation_view> all_views, HashSet<citation_view> curr_views)
	{
		ArrayList<Integer> view_ids = new ArrayList<Integer>();
		
		for(Iterator iter = curr_views.iterator(); iter.hasNext();)
		{
			citation_view view = (citation_view) iter.next();
			
			view_ids.add(all_views.indexOf(view));
		}
		
		return view_ids;
	}
	
	public static ArrayList<String> get_citation_view_names(ArrayList<citation_view> single_views)
	{
		ArrayList<String> names = new ArrayList<String>();
		
		for(int i = 0; i<single_views.size(); i++)
		{
			names.add(single_views.get(i).get_name());
		}
		
		return names;
	}
	
	public static HashSet<HashMap<String, HashSet<String>>> gen_citations_covering_set_level(HashMap<Tuple, HashMap<String, HashSet<String>>> citations, HashSet<Covering_set> curr_res, HashSet<Tuple> valid_view_mappings_schema_level)
	{
	    HashSet<HashMap<String, HashSet<String>>> full_citations = new HashSet<HashMap<String, HashSet<String>>>();
		
		for(Covering_set c_vector : curr_res)
		{
			HashMap<String, HashSet<String>> curr_full_citations = new HashMap<String, HashSet<String>>();
			
			for(citation_view view_mapping: c_vector.c_vec)
			{
				
			  Tuple tuple = view_mapping.get_view_tuple();
			  
				HashMap<String, HashSet<String>> curr_citations = citations.get(tuple);
				
				if(curr_full_citations.isEmpty())
				{
					Set<String> keys = curr_citations.keySet();
					
					for(Iterator iter = keys.iterator(); iter.hasNext();)
					{
						String key = (String)iter.next();
						
						HashSet<String> curr_values = new HashSet<String>();
						
						curr_values.addAll(curr_citations.get(key));
						
						curr_full_citations.put(key, curr_values);
					}
					
				}
				else
				{
					Set<String> keys = curr_citations.keySet();
					
					for(Iterator iter = keys.iterator(); iter.hasNext();)
					{
						String key = (String)iter.next();
						
						HashSet<String> curr_values = (HashSet<String>) curr_citations.get(key).clone();
						
						if(curr_full_citations.containsKey(key))
						{
							curr_full_citations.get(key).addAll(curr_values);
						}
						else
						{
							curr_full_citations.put(key, curr_values);
						}
					}
				}
			}
			
			full_citations.add(curr_full_citations);
		}
		
		
		
		return full_citations;
	}
	
	public static HashMap<Tuple, HashMap<String, HashSet<String>>> gen_citation_view_level(HashSet<Tuple> single_views, HashMap<Tuple, ArrayList<Head_strs>> lambda_values, HashMap<String, HashMap<String, String>> view_citation_query_mappings, HashMap<String, Query> citation_query_name_mappings, Connection c, PreparedStatement pst) throws SQLException
	{
		HashMap<Tuple, HashMap<String, HashSet<String>>> all_citations = new HashMap<Tuple, HashMap<String, HashSet<String>>>();
		
		for(Tuple tuple: single_views)
		{
		  HashSet<Head_strs> citation_info = new HashSet<Head_strs>();
		  
		  if(lambda_values.get(tuple) != null)
	        citation_info.addAll(lambda_values.get(tuple));
		  
			HashMap<String, HashSet<String>> citations = gen_citation_view_level(tuple, citation_info,  view_citation_query_mappings, citation_query_name_mappings, c, pst);
			
			all_citations.put(tuple, citations);
		}
		
		return all_citations;
		
	}
	
	static HashMap<String, HashSet<String>> gen_citation_view_level(Tuple single_view, HashSet<Head_strs> lambda_values, HashMap<String, HashMap<String, String>> view_citation_query_mappings, HashMap<String, Query> citation_query_name_mappings, Connection c, PreparedStatement pst) throws SQLException
	{
		HashMap<String, String> citation_queries_ids = view_citation_query_mappings.get(single_view.name);
		
		HashMap<String, Query> citation_queries = citation_query_name_mappings;
		
		HashMap<String, HashSet<String>> citations = new HashMap<String, HashSet<String>>();
		
		
		
//		if(tuple_level)
//		{
//			citation_queries_ids = Tuple_level_approach.get_citation_queries(single_view.name);
//			
//			citation_queries = Tuple_level_approach.citation_queries;
//			
//		}
//		else
//		{
//			
//			if(!schema_level)
//			{
//				citation_queries_ids = Semi_schema_level_approach.get_citation_queries(single_view.name);
//				
//				citation_queries = Semi_schema_level_approach.citation_queries;
//				
//			}
//			else
//			{
//				citation_queries_ids = Schema_level_approach.get_citation_queries(single_view.name);
//				
//				citation_queries = Schema_level_approach.citation_queries;
//			}
//			
//		}
		
		Set<String> keys = citation_queries_ids.keySet();
		
		for(Iterator iter = keys.iterator(); iter.hasNext();)
		{			
			String block_name = (String)iter.next();
			
			Query q = citation_queries.get(citation_queries_ids.get(block_name));
			
			String query_base = Query_converter.datalog2sql(q);
			
			if(q.lambda_term.size() > 0)
			{
//				citation_view_parametered curr_view = (citation_view_parametered) single_view;
				
				Vector<Integer> l_term_ids = new Vector<Integer>();
				
				for(int k = 0; k<q.lambda_term.size(); k++)
				{
					
					int id = single_view.lambda_terms.indexOf(q.lambda_term.get(k));
					
					l_term_ids.add(id);
				}
				
				String query = get_full_query_string(query_base, q, lambda_values, l_term_ids);
								
				gen_single_citations(query, block_name, citations, c, pst);
			}
			else
			{
				gen_single_citations(query_base, block_name, citations, c, pst);
			}
		}
		
		return citations;
	}
	
	static String get_full_query_string(String query_base, Query q, HashSet<Head_strs> lambda_values, Vector<Integer> l_term_ids)
	{
		String [] l_values = new String[l_term_ids.size()];
		
		
		
		String query = query_base;
		
		int num = 0;
		
		HashSet<Head_strs> cq_lambda_values = new HashSet<Head_strs>();
		
		for(Iterator iter = lambda_values.iterator(); iter.hasNext();)
		{
			Head_strs head_value = (Head_strs) iter.next();
			
			Vector<String> curr_cq_lambda_values = new Vector<String>();
			
			for(int k = 0; k<l_term_ids.size(); k++)
			{
				curr_cq_lambda_values.add(head_value.head_vals.get(l_term_ids.get(k)));
				
//				if(num >= 1)
//					l_values[k] += ",";
//				
//				if(l_values[k] != null)
//					l_values[k] += "'" + head_value.head_vals.get(l_term_ids.get(k)) + "'";
//				else
//					l_values[k] = "'" + head_value.head_vals.get(l_term_ids.get(k)) + "'";
			}
			
			Head_strs curr_cq_head_strs = new Head_strs(curr_cq_lambda_values);
			
			cq_lambda_values.add(curr_cq_head_strs);
		}
		
		for(Iterator iter = cq_lambda_values.iterator(); iter.hasNext();)
		{
			Head_strs head_value = (Head_strs) iter.next();
						
			for(int k = 0; k<head_value.head_vals.size(); k++)
			{				
				if(num >= 1)
					l_values[k] += ",";
				
				String value = head_value.head_vals.get(k);
				
				if(value!= null && value.contains("'"))
				{
					value = value.replaceAll("'", "''");
				}
				
				
				if(l_values[k] != null)
					l_values[k] += "'" + value + "'";
				else
					l_values[k] = "'" + value + "'";
			}
			
			num ++;
		}
		
		if(q.conditions == null || q.conditions.size() == 0)
		{
			query += " where (";
		}
		else
		{
			query += " and (";
		}
		
		for(int k = 0; k<l_term_ids.size(); k++)
		{
			
			if(k >= 1)
				query += ",";
			
			String l_name = q.lambda_term.get(k).arg_name;
			
			String table_name = l_name.substring(0, l_name.indexOf(populate_db.separator));
			
			String attr_name = l_name.substring(l_name.indexOf(populate_db.separator) + 1, l_name.length());
			
			query += "cast (" + table_name + "." + attr_name + " as text)";
		}
		
		query += ") in (select * from unnest(";
		
		for(int k = 0; k<l_term_ids.size(); k++)
		{
			if(k >= 1)
				query += ",";
			
			query += "ARRAY[" + l_values[k] + "]";
		}
		
		query += "))";
		
		return query;
	}
	
	static void gen_single_citations(String sql, String block_name, HashMap<String, HashSet<String>> citations, Connection c, PreparedStatement pst) throws SQLException
	{
		pst = c.prepareStatement(sql);
		
		ResultSet rs = pst.executeQuery();
		
		HashSet<String> values = new HashSet<String>();
		
		ResultSetMetaData meta = rs.getMetaData();
		
		int col_num = meta.getColumnCount();
		
		while(rs.next())
		{
			
			String value = new String();
			
			for(int i = 0; i<col_num; i++)
			{
				if(i >= 1)
					value += " ";
				
				value += rs.getString(i + 1);
			}
			
			values.add(value);
			
//			if(block_name.equals("author"))
//			{
//				values.add(rs.getString(1) + " " + rs.getString(2));
//			}
			
		}
		
		citations.put(block_name, values);
		
	}
	
	static ArrayList<citation_view> get_single_citation_views(ArrayList<Covering_set> curr_res, ArrayList<String> view_keys)
	{
		ArrayList<citation_view> views = new ArrayList<citation_view>();
		
		HashSet<String> view_names = new HashSet<String>();
		
		for(int i = 0; i<curr_res.size(); i++)
		{
			for(citation_view view_mapping: curr_res.get(i).c_vec)
			{
				
				citation_view c = view_mapping;
				
				String key = c.get_name() + populate_db.separator + c.get_table_name_string();
				
				if(!view_names.contains(key))
				{
					
					views.add(c);
					
					view_names.add(key);
					
					view_keys.add(key);
				}
				
			}
		}
		
		
		
		return views;
	}
	
	static ArrayList<citation_view> get_single_citation_views(HashSet<Covering_set> curr_res, ArrayList<String> view_keys)
	{
		ArrayList<citation_view> views = new ArrayList<citation_view>();
		
		HashSet<String> view_names = new HashSet<String>();
		
		for(Iterator iter = curr_res.iterator(); iter.hasNext();)
		{
			
			Covering_set covering_set = (Covering_set) iter.next();
			
			for(citation_view view_mapping: covering_set.c_vec)
			{
				
				citation_view c = view_mapping;
				
				String key = c.get_name() + populate_db.separator + c.get_table_name_string();
				
				if(!view_names.contains(key))
				{
					
					views.add(c);
					
					view_names.add(key);
					
					view_keys.add(key);
				}
				
			}
		}
		
		
		
		return views;
	}
	
	
//	static void convert_covering_set2citation(int seq, ArrayList<Covering_set> curr_res, ArrayList<HashMap<String, HashSet<String>>> author_lists, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, ArrayList<ArrayList<String>>> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, Connection c, PreparedStatement pst, boolean tuple_level) throws ClassNotFoundException, SQLException
//	{
//		
//		if(seq == 0)
//		{
//			for(int i = 0; i<curr_res.size(); i++)
//			{
//				Covering_set covering_set = curr_res.get(i);
//				
//				HashMap<String, HashSet<String>> citations = gen_citation0.join_covering_sets(covering_set, c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
//				
//				author_lists.add(citations);
//			}
//		}
//		else
//		{
//			for(int i = 0; i<curr_res.size(); i++)
//			{
//				Covering_set covering_set = curr_res.get(i);
//				
//				HashMap<String, HashSet<String>> citations = gen_citation0.join_covering_sets(covering_set, c, pst, view_list, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_author_mapping);
//				
//				merge_citations(author_lists.get(i), citations);
//			}
//		}
//		
//	}
	
	static void merge_citations(HashMap<String, HashSet<String>> citations1, HashMap<String, HashSet<String>> citations2)
	{
		Set<String> keys = citations2.keySet();
		
		for(Iterator iter = keys.iterator(); iter.hasNext();)
		{
			String key = (String) iter.next();
			
			HashSet<String> values = citations2.get(key);
			
			if(citations2.containsKey(key))
			{
				citations2.get(key).addAll(values);
			}
			else
			{
				citations2.put(key, values);
			}
		}
	}

//    public static HashSet<String> gen_citation_entire_query(ResultSet rs, boolean tuple_level, boolean schema_level, ArrayList<Covering_set> covering_sets, int start_pos, HashMap<String, Integer> max_num, Connection c, PreparedStatement pst) throws SQLException
//    {
//        ArrayList<String> view_keys = new ArrayList<String>();
//        
//        ArrayList<citation_view> single_views = get_single_citation_views(covering_sets, view_keys);
//        
//        ArrayList<String> single_view_names = get_citation_view_names(single_views);
//                        
//        int tuple_num = 0;
//        
//        if(tuple_level)
//        {
//            tuple_num = Tuple_level_approach.tuple_num;
//        }
//        else
//        {
//            if(!schema_level)
//            {
//                tuple_num = Semi_schema_level_approach.tuple_num;
//            }
//            else
//            {
//                tuple_num = Schema_level_approach.tuple_num;
//            }
//        }
//        
//        for(int i = 0; i<tuple_num; i++)
//        {
//            rs.absolute(i + 1);
//            
//            if(tuple_level)
//                Tuple_level_approach.get_views_parameters(single_views, rs, start_pos, lambda_values);
//            else
//            {
//                if(!schema_level)
//                  Semi_schema_level_approach.get_views_parameters(single_views, rs, start_pos, lambda_values);
//                else
//                    Schema_level_approach.get_views_parameters(single_views, rs, start_pos, lambda_values);
//            }
//            
////          convert_covering_set2citation(i, curr_res, author_lists, view_query_mapping, query_lambda_str, author_mapping, max_num, query_ids, view_list, c, pst, tuple_level);
//            
//            
//        }
//        
//        
//        
//        ArrayList<HashMap<String, HashSet<String>>> citations = gen_citation_view_level(single_views, lambda_values, tuple_level, schema_level, c, pst);
//                
//        full_citations = gen_citations_covering_set_level(citations, covering_sets, single_view_names, view_keys);
//        
//        return gen_citations(full_citations, max_num);
//    }
//    
    static HashSet<HashMap<String, HashSet<String>>> gen_citations_covering_set_level(ArrayList<HashMap<String, HashSet<String>>> citations, ArrayList<Covering_set> curr_res, ArrayList<String> single_view_names, ArrayList<String> view_keys)
    {
      HashSet<HashMap<String, HashSet<String>>> full_citations = new HashSet<HashMap<String, HashSet<String>>>();
        
        for(int i = 0; i<curr_res.size(); i++)
        {
            Covering_set c_vector = curr_res.get(i);
            
            HashMap<String, HashSet<String>> curr_full_citations = new HashMap<String, HashSet<String>>();
            
            for(citation_view view_mapping: c_vector.c_vec)
            {
                
                String view_key = view_mapping.get_name() + populate_db.separator + view_mapping.get_table_name_string();
                
                int id = view_keys.indexOf(view_key);
                
                HashMap<String, HashSet<String>> curr_citations = citations.get(id);
                
                if(curr_full_citations.isEmpty())
                {
                    Set<String> keys = curr_citations.keySet();
                    
                    for(Iterator iter = keys.iterator(); iter.hasNext();)
                    {
                        String key = (String)iter.next();
                        
                        HashSet<String> curr_values = new HashSet<String>();
                        
                        curr_values.addAll(curr_citations.get(key));
                        
                        curr_full_citations.put(key, curr_values);
                    }
                    
                }
                else
                {
                    Set<String> keys = curr_citations.keySet();
                    
                    for(Iterator iter = keys.iterator(); iter.hasNext();)
                    {
                        String key = (String)iter.next();
                        
                        HashSet<String> curr_values = (HashSet<String>) curr_citations.get(key).clone();
                        
                        if(curr_full_citations.containsKey(key))
                        {
                            curr_full_citations.get(key).addAll(curr_values);
                        }
                        else
                        {
                            curr_full_citations.put(key, curr_values);
                        }
                    }
                }
            }
            
            full_citations.add(curr_full_citations);
        }
        
        
        
        return full_citations;
    }
	
//	public static HashSet<String> do_agg_intersection(ResultSet rs, HashMap<int[], ArrayList<citation_view_vector>> c_view_map, int start_pos, ArrayList<HashMap<String, Integer>> view_query_mapping, ArrayList<Lambda_term[]> query_lambda_str, HashMap<String, Unique_StringList> author_mapping, HashMap<String, Integer> max_num, IntList query_ids, StringList view_list, HashMap<String, String> view_citation_mapping, Connection c, PreparedStatement pst, boolean tuple_level) throws SQLException, ClassNotFoundException, JSONException
//	{
//		
//		HashMap<String, Boolean> full_flag = new HashMap<String, Boolean>();
//		
//		for(int i = 0; i<populate_db.available_block.length; i++)
//		{
//			full_flag.put(populate_db.available_block[i], false);
//		}
//		
//		Set<int[]> intervals = c_view_map.keySet();
//		
//		int index = 0;
//		
//		rs.beforeFirst();
//		
//		ArrayList<HashMap<String, HashSet<String>>> author_lists = new ArrayList<HashMap<String, HashSet<String>>>();
//		
//		ArrayList<citation_view_vector> curr_res = new ArrayList<citation_view_vector>();
//		
//		HashSet<String> citation_list = new HashSet<String>();
//		
//		for(Iterator iter = intervals.iterator(); iter.hasNext();)
//		{
//			int [] interval = (int[]) iter.next();
//			
//			ArrayList<citation_view_vector> c_view = c_view_map.get(interval);
//			
//			for(int i = interval[0]; i < interval[1]; i ++)
//			{
//				
//				
//				if(selected_row_ids.get(i) < interval[1] && selected_row_ids.get(i) >= interval[0])
//				{
//					rs.absolute(i + 1);
//					
//					if(tuple_level)
//						Tuple_reasoning1_test.update_valid_citation_combination(c_view, rs, start_pos);
//					else
//						Tuple_reasoning2_test.update_valid_citation_combination(c_view, rs, start_pos);
//					
//
//					
//					do_aggregate(curr_res, c_view, i, author_lists, view_query_mapping, author_mapping, max_num, query_ids, query_lambda_str, view_list, full_flag, view_citation_mapping, tuple_level, c, pst);
//					
////					System.out.println(curr_res);
//				}
//			}
//		}
//		
//		System.out.println(curr_res);
//		
//		return gen_citations(author_lists, max_num);
//	}

}
