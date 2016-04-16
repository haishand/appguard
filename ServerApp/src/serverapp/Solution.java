package serverapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {
    
    public List<List<Integer>> permute(int[] nums) {
        int n = nums.length;
        List<List<Integer>> ll = new ArrayList<List<Integer>>();
        int[] aa = new int[n];
        HashMap flag = new HashMap();
        permute_r(0, ll, aa, flag, nums, n);
        return ll;
    }
    
    public void permute_r(int d, List<List<Integer>> ll, int[] aa, HashMap flag, int[] nums, int n) {
        if (d == n) {
            List<Integer> l = new ArrayList<Integer>();
            for (int i=0; i<n; i++) {
                l.add(aa[i]);
            }
            ll.add(l);
            return;
        }
        
        for (int i=0; i<n; i++) {
            if (flag.containsKey(nums[i])) continue;
            
            aa[d] = nums[i];
            flag.put(nums[i], 1);
            permute_r(d+1, ll, aa, flag, nums, n);
            flag.remove(nums[i]);
        }
    }
    
    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        Solution s = new Solution();
        
        List<List<Integer>> al = s.permute(a);
        for (List<Integer> l : al) {
            for (int i=0; i<l.size(); i++) {
                System.out.print(l.get(i) + " ");
            }
            System.out.println();
        }
    }
}