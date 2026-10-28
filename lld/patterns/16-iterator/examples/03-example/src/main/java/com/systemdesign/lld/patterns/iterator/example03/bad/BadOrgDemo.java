package com.systemdesign.lld.patterns.iterator.example03.bad;

public class BadOrgDemo {
    public static void main(String[] args) {
        BadEmployee ceo = new BadEmployee("Alice Smith", "Chief Executive Officer", 450000);
        BadEmployee cto = new BadEmployee("Bob Johnson", "Chief Technology Officer", 320000);
        BadEmployee cfo = new BadEmployee("Carol Williams", "Chief Financial Officer", 310000);

        BadEmployee engDirector = new BadEmployee("Dave Miller", "Director of Engineering", 220000);
        BadEmployee leadDev = new BadEmployee("Eve Davis", "Tech Lead", 160000);

        ceo.addSubordinate(cto);
        ceo.addSubordinate(cfo);
        cto.addSubordinate(engDirector);
        engDirector.addSubordinate(leadDev);

        System.out.println("=== BAD DFS Traversal ===");
        StringBuilder dfsOutput = new StringBuilder();
        ceo.printDfs(0, dfsOutput);
        System.out.println(dfsOutput);

        System.out.println("=== BAD BFS Traversal ===");
        StringBuilder bfsOutput = new StringBuilder();
        ceo.printBfs(bfsOutput);
        System.out.println(bfsOutput);
    }
}
