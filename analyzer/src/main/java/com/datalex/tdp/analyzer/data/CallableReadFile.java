package com.datalex.tdp.analyzer.data;

import com.datalex.tdp.analyzer.jxpath.XmlTransform;
import org.w3c.dom.Node;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public class CallableReadFile implements Callable<Node>
{
    private Path path;

    public Path getPath()
    {
        return path;
    }

    public CallableReadFile(Path p)
    {
        this.path = p;
    }

    @Override
    public Node call() throws Exception
    {
        XmlTransform xml = new XmlTransform();
        Node n = xml.loadFile(path);
        return n;
    }
}
