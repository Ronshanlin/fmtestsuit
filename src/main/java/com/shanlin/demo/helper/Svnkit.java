package com.shanlin.demo.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNAuthenticationException;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.shanlin.demo.bean.SvnNode;

public class Svnkit {
    
    public static boolean testAuth(String httpUrl,String username, String passpord){
        try {
            SVNRepository repository = getRepository(httpUrl, username, passpord);
            repository.testConnection();
        } catch (SVNException e) {
            if (e instanceof SVNAuthenticationException) {
                return false;
            }else {
                e.printStackTrace();
                return false;
            }
        }
        
        return true;
    }
    
    public static SvnNode getSvnRepository(String httpUrl,String username, String passpord, String path){
        SvnNode node = new SvnNode();
        node.setKind(0);
        node.setName(path);
        node.setParentPath(null);
        
        try {
            SVNRepository repository = getRepository(httpUrl, username, passpord);
            // 
            listEntries(repository, path, node);
            
            return node;
        } catch (SVNException e) {
            e.printStackTrace();
        }
        
        return node;
    }
    
    public static ByteArrayOutputStream getFile(String httpUrl,String username, String passpord, String path){
        ByteArrayOutputStream stream = null;
        try {
            SVNRepository repository = getRepository(httpUrl, username, passpord);
            stream = new ByteArrayOutputStream();
            // 
            repository.getFile(path, -1, null, stream);
            
            return stream;
        } catch (SVNException e) {
            e.printStackTrace();
        } finally{
            if (stream!=null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 功能描述: 创建svn仓库<br>
     *
     * @param httpUrl
     * @param username
     * @param passpord
     * @return
     * @throws SVNException
     */
    private static SVNRepository getRepository(String httpUrl,String username, String passpord) throws SVNException{
        // 创建仓库
        DAVRepositoryFactory.setup();
        SVNURL url = SVNURL.parseURIEncoded(httpUrl);
        SVNRepository repository = DAVRepositoryFactory.create(url);
        
        // 授权
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, passpord.toCharArray());
        repository.setAuthenticationManager(authManager);
        
        return repository;
    }
    
    @SuppressWarnings("unchecked")
    private static void listEntries(SVNRepository repository, String path,SvnNode node)
            throws SVNException {
        if (node == null) {
           return;
        }
        
        Collection<SVNDirEntry> entries = repository.getDir(path, -1, null,(Collection<SVNDirEntry>)null);
        
        List<SvnNode> svnNodes = node.getNodes();
        SvnNode subNode = null;
        
        Iterator<SVNDirEntry> iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = iterator.next();
//            System.out.println("/" + (path.equals("") ? "" : path + "/")
//                    + entry.getName() + " (author: '" + entry.getAuthor()
//                    + "'; revision: " + entry.getRevision() + "; date: " + format.format(entry.getDate()) + ")");
            subNode = new SvnNode();
            subNode.setKind(entry.getKind().getID());
            subNode.setName(entry.getName());
            subNode.setParentPath(path);
            svnNodes.add(subNode);
            /*
             * Checking up if the entry is a directory.
             */
            if (entry.getKind() == SVNNodeKind.DIR) {
                String subPath = (path.equals("")) ? entry.getName() : path+"/"+entry.getName();
                listEntries(repository, subPath, subNode);
            }
        }
    }
}
