package com.company;

import java.awt.*;
import java.util.*;

class DNF {
    byte[] dnf;
    boolean marked;
    int countOfOne;

    public DNF(byte[] dnf) {
        this.dnf = dnf;
        this.marked = false;
        this.countOfOne = this.getCountOfOne();
    }

    int getDistance(DNF bytes) {
        int count = 0;
        for (int i = 0; i < bytes.dnf.length; i++) {
            if (bytes.dnf[i] != this.dnf[i]) {
                count++;
            }
        }
        return count;
    }

    int getCountOfOne() {
        int count = 0;
        for (int i = 0; i < dnf.length; i++) {
            if (dnf[i] == 1)
                count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return Arrays.toString(dnf);
    }

    boolean equals(DNF term) {
        for (int i = 0; i < dnf.length; i++) {
            if (dnf[i] != term.dnf[i])
                return false;
        }
        return true;
    }
}

class Terms {
    LinkedList<DNF> terms;

    public Terms() {
        terms = new LinkedList<>();
    }

    public Terms(LinkedList<DNF> terms) {
        this.terms = terms;
    }

    public Terms(String f, int n) {
        String temp;
        terms = new LinkedList<>();
        for (int i = 0; i < f.length(); i++) {
            if (f.charAt(i) == '1') {
                temp = String.format("%0" + n + "d", Integer.parseInt(Integer.toBinaryString(i)));
                byte[] item = new byte[n];
                for (int j = 0; j < n; j++) {
                    item[j] = (byte) (temp.charAt(j) - '0');
                }
                DNF term = new DNF(item);
                terms.add(term);
            }
        }
    }


    public void add(LinkedList<DNF> terms) {
        this.terms.addAll(terms);
    }


    LinkedList<DNF> getSubList(int countOfOne) {
        LinkedList<DNF> subList = new LinkedList<>();
        Iterator<DNF> iterator = terms.iterator();
        DNF term;
        while (iterator.hasNext()) {
            term = iterator.next();
            if (term.countOfOne == countOfOne) {
                subList.add(term);
            }
        }
        return subList;
    }

    @Override
    public String toString() {
        Iterator<DNF> iterator = terms.iterator();
        StringBuilder builder = new StringBuilder();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
        }
        return builder.toString();
    }

    LinkedList<DNF> glue(LinkedList<DNF> terms1, LinkedList<DNF> terms2) {
        LinkedList<DNF> gluedList = new LinkedList<>();
        Iterator<DNF> iterator1 = terms1.iterator();
        DNF term1;
        DNF term2;
        DNF newTerm;
        while (iterator1.hasNext()) {
            term1 = iterator1.next();
            Iterator<DNF> iterator2 = terms2.iterator();
            while (iterator2.hasNext()) {
                term2 = iterator2.next();
                if (term1.getDistance(term2) == 1) {
                    byte[] bytes = new byte[term1.dnf.length];
                    int t = 0;
                    for (int i = 0; i < term1.dnf.length; i++) {
                        if (term1.dnf[i] != 2 && term2.dnf[i] != 2) {
                            if (term1.dnf[i] == term2.dnf[i]) {
                                bytes[i] = term1.dnf[i];
                                t++;
                            } else {
                                bytes[i] = 2;
                                t++;
                            }
                        } else if (term1.dnf[i] == 2 && term2.dnf[i] == 2) {
                            bytes[i] = 2;
                            t++;
                        }
                    }
                    if (t == term1.dnf.length) {
                        term1.marked = true;
                        term2.marked = true;
                        newTerm = new DNF(bytes);
                        gluedList.add(newTerm);
                    }
                }
            }
        }
        return gluedList;
    }

    static LinkedList<DNF> clear(LinkedList<DNF> list) {
        HashSet<Integer> indexes = new HashSet<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    indexes.add(j);
                }
            }
            int r = 0;
            for (Iterator<Integer> iterator1 = indexes.iterator(); iterator1.hasNext(); ) {
                list.remove(iterator1.next() - r);
                r++;
            }
            indexes = new HashSet<>();
        }
        return list;
    }
}

class DNFComparator implements Comparator<DNF> {

    @Override
    public int compare(DNF o1, DNF o2) {
        return Integer.parseInt(Integer.toString(o1.dnf[0]) + Integer.toString(o1.dnf[1]) +
                Integer.toString(o1.dnf[2]) + Integer.toString(o1.dnf[3])) -
                Integer.parseInt(Integer.toString(o2.dnf[0]) + Integer.toString(o2.dnf[1]) +
                        Integer.toString(o2.dnf[2]) + Integer.toString(o2.dnf[3]));
    }
}

class LengthComparator implements Comparator<ArrayList<Integer>> {

    @Override
    public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
        return o1.size() - o2.size();
    }

}


public class Main {
    static LinkedList<DNF> solution;
    static boolean table[][];
    static int n = 7;
    static int bee = 40;
    static int eliteBee = 80;
    static int generation = 50;
    static int minFitness;
    static int maxY = 130;
    static int minY = 110;
    static int threshold = 2;
    //static String f = "0110011000110011";     // 7
    //static String f = "1000110111011100";     // 10
    //static String f = "1100010100000011";     // 9
    //static String f = "11110000000000001101000011000000";
    //static String f = "1100000000010011110001010011001111111101111111101111110111111111";     // 25
    //static String f = "1000000100010011110101010011000000001101111001110001010111111111";     // 51
    //static String f = "0100110101010011010001010011001010010101110101001001101010110101";      // 58
    static String f = "1101011011101001010011011010101110101011010100001100100110010100110001010101101100000000000000000000000000000000000000000000000";     //115
    //static String f = "00000000111010010000110000100011101000111101000011001111100101001100010100000011000000000000000000000000000000000000000000000000";     //85
    //static String f = "1101011011101001010011011010101110101011010100001100100110010100110001010101101100000000000000000000000000000000000000000000000";     //115
    //static String f = "0000000000000000000000000000000000000000000000000000000000000000000000000000000001111111111111111111111111111111111111111111111";      //22
    //static String f = "0000000000000100100001100001000111010000000100001100111110010100110001010000001100000000111010010000110000100011101111111101111";      //122
    //static String f = "1110001011000111100001100001000111010001111100001111111110010100000001010111001101011000111010010011110000100011101110001101100";      //134
    //static String f = "1110001011000100100001100001000111010001110100001100111110010100110001010111001101011000111010010010110000100011101110001101100";      //7 138
    //static String f = "11100010110001000000011000000001110100000001000011001111100101001100000001110011000000001110100000001100000000111011100011011001110001011000100100001100001000111110001110100001100111110010100110001010111001101111000111110010011110000100011111111111111111";      //270
    //static String f = "10100010110001000100011011010001110100000001000011001100100101001100011101110011010011001110100011101100001000111011100011011001110001011000100101101100101000111110001110101001100111110010100110001010111001101111000111110010011110010100011111111110001111";      //303


    static LinkedList<DNF> getSimpleImplicate() {
        Terms terms = new Terms(f, n);
        LinkedList<DNF> subList1;
        LinkedList<DNF> subList2;
        LinkedList<DNF> gluedList;
        LinkedList<DNF> solution = new LinkedList<>();
        Terms newLevelTerms = null;
        for (int k = 0; k < n; k++) {
            newLevelTerms = new Terms();
            for (int i = 0; i < n; i++) {
                subList1 = terms.getSubList(i);
                subList2 = terms.getSubList(i + 1);
                if (!subList1.isEmpty() && !subList2.isEmpty()) {
                    gluedList = terms.glue(subList1, subList2);
                    newLevelTerms.add(gluedList);
                }
            }
            Iterator<DNF> iterator = terms.terms.iterator();
            DNF term;
            while (iterator.hasNext()) {
                term = iterator.next();
                if (!term.marked) {
                    solution.add(term);
                }
            }
            terms = new Terms(Terms.clear(newLevelTerms.terms));
            System.out.println();
            System.out.println();
        }
        solution.addAll(newLevelTerms.terms);
        Collections.sort(solution, new DNFComparator());
        Iterator<DNF> iterator = solution.iterator();
        return solution;
    }

    static boolean[][] getTable(LinkedList<DNF> solution) {
        String temp;
        LinkedList<byte[]> vars = new LinkedList<>();
        for (int i = 0; i < f.length(); i++) {
            if (f.charAt(i) == '1') {
                temp = String.format("%0" + n + "d", Integer.parseInt(Integer.toBinaryString(i)));
                byte[] item = new byte[n];
                for (int j = 0; j < n; j++) {
                    item[j] = (byte) (temp.charAt(j) - '0');
                }
                vars.add(item);
                System.out.println(temp);
            }
        }
        boolean[][] table = new boolean[solution.size()][vars.size()];
        byte[] bytes1;
        byte[] bytes2;
        int l = 0;
        boolean b = true;
        for (Iterator<DNF> solIter = solution.iterator(); solIter.hasNext(); ) {
            bytes1 = solIter.next().dnf;
            for (Iterator<byte[]> varIter = vars.iterator(); varIter.hasNext(); ) {
                bytes2 = varIter.next();
                for (int i = 0; i < bytes1.length; i++) {
                    if (!(bytes1[i] == bytes2[i] || bytes1[i] == 2)) {
                        b = false;
                    }
                }
                if (b) {
                    table[l][vars.indexOf(bytes2)] = true;
                }
                b = true;
            }
            l++;
        }
        System.out.println();
        Iterator<byte[]> iterator1 = vars.iterator();
        System.out.print("\t\t\t\t\t");
        byte[] t;
        while (iterator1.hasNext()) {
            t = iterator1.next();
            if (n == 4)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]+ "\t");//+ t[7]  + "" + t[4] + "" + t[5]+ ""+ t[6] + ""
            if (n == 5)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]  + "" + t[4]+ "\t");// + "" + t[5]+ ""+ t[6] + "" + t[7]
            if (n == 6)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]  + "" + t[4] + "" + t[5]+ "\t");//+ ""+ t[6] + "" + t[7]
            if (n == 7)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]  + "" + t[4] + "" + t[5]+ ""+ t[6]+ "\t");// + "" + t[7]
            if (n == 8)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]  + "" + t[4] + "" + t[5]+ ""+ t[6] + "" + t[7]+ "\t");//
            if (n == 9)
                System.out.print(t[0] + "" + t[1] + "" + t[2] + "" + t[3]  + "" + t[4] + "" + t[5]+ ""+ t[6] + "" + t[7] + "" + t[8]+ "\t");//
        }
        System.out.println();
        Iterator<DNF> iterator = solution.iterator();
        for (int i = 0; i < table.length; i++) {
            System.out.print(i + "\t");
            if (iterator.hasNext()) {
                System.out.print(iterator.next() + "\t");
            }
            for (int j = 0; j < table[0].length; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
        return table;
    }

    static ArrayList<ArrayList<Integer>> getIndexes(boolean[][] table) {
        ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();
        for (int i = 0; i < table[0].length; i++) {
            ArrayList<Integer> array = new ArrayList<>();
            for (int j = 0; j < table.length; j++) {
                if (table[j][i]) {
                    array.add(j);
                }
            }
            indexes.add(array);
        }
        int h = 0;
        Collections.sort(indexes, new LengthComparator());
        return indexes;

    }

    static HashSet<Integer> getBest(HashSet<HashSet<Integer>> set) {
        HashSet<Integer> best = new HashSet<>();
        HashSet<Integer> item;
        int min = -1;
        int count = 0;
        for (Iterator<HashSet<Integer>> iterator = set.iterator(); iterator.hasNext(); ) {
            item = iterator.next();
            for (Iterator<Integer> iterator1 = item.iterator(); iterator1.hasNext(); ) {
                DNF dnf = solution.get(iterator1.next());
                for (int i = 0; i < dnf.dnf.length; i++) {
                    if (dnf.dnf[i] == 0 || dnf.dnf[i] == 1) {
                        count++;
                    }
                }
            }
            if (min == -1) {
                min = count;
                best = item;
            } else if (count < min) {
                min = count;
                best = item;
            }
            count = 0;
        }
        minFitness = min;
        return best;
    }

    static HashSet<HashSet<Integer>> getBestSet(HashSet<HashSet<Integer>> set) {
        int minLen = -1;
        HashSet<HashSet<Integer>> bestSet = new HashSet<>();
        int size;
        for (Iterator<HashSet<Integer>> iterator = set.iterator(); iterator.hasNext(); ) {
            size = iterator.next().size();
            if (minLen == -1) {
                minLen = size;
            } else if (size < minLen)
                minLen = size;
        }
        for (Iterator<HashSet<Integer>> iterator = set.iterator(); iterator.hasNext(); ) {
            HashSet<Integer> item = iterator.next();
            if (item.size() == minLen)
                bestSet.add(item);
        }
        return bestSet;
    }

    static HashSet<HashSet<Integer>> getBestSet(HashSet<HashSet<Integer>> set, int n) {
        int minLen = -1;
        HashSet<HashSet<Integer>> bestSet = new HashSet<>();
        int size;
        for (Iterator<HashSet<Integer>> iterator = set.iterator(); iterator.hasNext(); ) {
            size = iterator.next().size();
            if (minLen == -1) {
                minLen = size;
            } else if (size < minLen)
                minLen = size;
        }
        int i = 1;
        for (Iterator<HashSet<Integer>> iterator = set.iterator(); iterator.hasNext(); ) {
            if (i == n)
                break;
            HashSet<Integer> item = iterator.next();
            if (item.size() == minLen){
                bestSet.add(item);
                i++;
            }
        }
        return bestSet;
    }

static void print(LinkedList<DNF> answer){
    DNF myDNF;
    int count = 0;
    int g = 0;
    for (Iterator<DNF> iterator = answer.iterator(); iterator.hasNext(); ) {
        myDNF = (iterator.next());
        System.out.print("(");
        for (int i = 0; i < myDNF.dnf.length; i++) {
            if (myDNF.dnf[i] == 0) {
                count++;
                if (i > 1 || i == 1 && myDNF.dnf[0] !=2){
                    System.out.print(" ");
                }
                System.out.print("!x" + i );
                if (i < myDNF.dnf.length - 1){
                    System.out.print(" *");
                }
            } else if (myDNF.dnf[i] == 1) {
                count++;
                if (i > 1 || i == 1 && myDNF.dnf[0] !=2){
                    System.out.print(" ");
                }
                System.out.print("x" + i);
                if (i < myDNF.dnf.length - 1){
                    System.out.print(" *");
                }
            }
        }
        System.out.print(")");
        if (iterator.hasNext()) {
            System.out.print(" v ");
        }
        g++;
        if (g > 3){
            System.out.println();
            g = 0;
        }
    }
    System.out.println("\ncount " + count);
}

    public static void main(String[] args) {
        System.out.println(f.length());
        solution = getSimpleImplicate();
        table = getTable(solution);
        ArrayList<ArrayList<Integer>> indexes = getIndexes(table);
        ArrayList<Integer> arrayList;
        HashSet<Integer> set = new HashSet<>();
        for (Iterator<ArrayList<Integer>> iterator = indexes.iterator(); iterator.hasNext(); ) {
            arrayList = iterator.next();
            if (arrayList.size() == 1)
                set.add(arrayList.get(0));
        }
        HashSet<DNF> first = new HashSet<>();
        int firstFitness = 0;
        for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext(); ) {
            DNF dnf = solution.get(iterator.next());
            first.add(dnf);
            for(int i = 0; i < dnf.dnf.length; i++){
                if(dnf.dnf[i] == 0 || dnf.dnf[i] ==1)
                    firstFitness++;
            }
        }
        Integer idx;
        for (Iterator<ArrayList<Integer>> iterator1 = indexes.iterator(); iterator1.hasNext(); ) {
            arrayList = iterator1.next();
            for (Iterator<Integer> iterator2 = arrayList.iterator(); iterator2.hasNext(); ) {
                idx = iterator2.next();
                if (set.contains(idx)) {
                    iterator1.remove();
                    break;
                }
            }
        }
        HashSet<ArrayList<Integer>> newIndexes = new HashSet<>();
        for (Iterator<ArrayList<Integer>> iterator = indexes.iterator(); iterator.hasNext(); ) {
            arrayList = iterator.next();
            newIndexes.add(arrayList);
        }
        indexes = new ArrayList<>();
        for (Iterator<ArrayList<Integer>> iterator = newIndexes.iterator(); iterator.hasNext(); ) {
            arrayList = iterator.next();
            indexes.add(arrayList);
        }

        StdDraw.setCanvasSize(1900, 1000);
        StdDraw.setXscale(0, generation - 1);
        StdDraw.setYscale(0, maxY-minY);
        StdDraw.line(0,0, generation - 1,0);
        StdDraw.line(0,0,0,maxY-minY);
        for (int i = 5; i <= generation; i += 5) {
            StdDraw.text(i, 0, Integer.toString(i));
        }
        int step = 1;
        for (int i = minY; i <= maxY; i += step) {
            StdDraw.text(0, i - minY, Integer.toString(i));
        }
        StdDraw.setPenColor(Color.lightGray);
        for (int i = 0; i <= generation; i+=5){
            StdDraw.line(i, 0, i, maxY);
        }
        for (int i = minY; i <= maxY+1; i+=step){
            StdDraw.line(0, i - minY, generation,i- minY);
        }
        StdDraw.setPenColor(Color.black);
        StdDraw.setPenRadius(0.01);
        HashSet<Integer> dnf;
        HashSet<HashSet<Integer>> listDNF = new HashSet<>();
        HashSet<HashSet<Integer>> newListDNF;
        HashSet<HashSet<Integer>> bestSet;
        ArrayList<ArrayList<Integer>> copyIndexes;
        for (int k = 0; k < generation; k++){
            for (int i = 0; i < eliteBee; i++) {
                copyIndexes = new ArrayList<>(indexes);
                dnf = new HashSet<>();
                int h = 0;
                int pos;
                for (int j = 0; j < copyIndexes.size(); j++){
                    ArrayList<Integer> integers = copyIndexes.get(j);
                    int index = (int) (Math.random() * integers.size());
                    pos = integers.get(index);
                    dnf.add(pos);
                    for (int l = j + 1; l < copyIndexes.size();){
                        if (copyIndexes.get(l).contains(pos)){
                            copyIndexes.remove(l);
                        } else {
                            l++;
                        }
                    }
                }
                listDNF.add(dnf);
            }
            ArrayList<Integer> array;
            Integer item;
            boolean[] isCovered = new boolean[newIndexes.size()];
            for (int j = 0; j < bee; j++) {
                newListDNF = new HashSet<>();
                HashSet<Integer> currentSet;
                HashSet<Integer> integers;
                for (Iterator<HashSet<Integer>> iterator = listDNF.iterator(); iterator.hasNext(); ) {
                    currentSet = iterator.next();
                    integers = new HashSet<>();
                    array = new ArrayList<>(currentSet);
                    int index = (int) (Math.random() * array.size());
                    if (array.size()>0){
                        array.remove(index);
                    }
                    for (int i = 0; i < isCovered.length; i++) {
                        isCovered[i] = false;
                    }
                    for (Iterator<Integer> iterator1 = array.iterator(); iterator1.hasNext(); ) {
                        item = iterator1.next();
                        integers.add(item);
                        int h = 0;
                        for (Iterator<ArrayList<Integer>> iterator2 = newIndexes.iterator(); iterator2.hasNext(); ) {
                            ArrayList<Integer> integers1 = iterator2.next();
                            for (Iterator<Integer> iterator3 = integers1.iterator(); iterator3.hasNext(); ) {
                                Integer temp = iterator3.next();
                                if (item == temp) {
                                    isCovered[h] = true;
                                }
                            }
                            h++;
                        }
                    }
                    boolean b = true;
                    for (int i = 0; i < isCovered.length; i++) {
                        if (!isCovered[i]) {
                            b = false;
                        }
                    }
                    if (b) {
                        newListDNF.add(integers);
                    }
                }
                listDNF.addAll(getBestSet(newListDNF, threshold));//
            }
            bestSet = getBestSet(listDNF);
            getBest(bestSet);
            if (minFitness+ firstFitness == 138){
                StdDraw.setPenColor(Color.RED);
            } else {
                StdDraw.setPenColor(Color.black);
            }
            StdDraw.point(k, (firstFitness + minFitness - minY));
            listDNF = new HashSet<>(bestSet);//, threshold
        }
        HashSet<Integer> best = (getBest(getBestSet(listDNF)));
        LinkedList<DNF> answer = new LinkedList<>(first);
        Integer integer;
        for (Iterator<Integer> iterator = best.iterator(); iterator.hasNext(); ) {
            integer = iterator.next();
            answer.add(solution.get(integer));
        }
        System.out.println("Answer:");
        print(answer);
    }
}


