package org.graphwalker.core.generator;

/*
 * #%L
 * GraphWalker Core
 * %%
 * Copyright (C) 2005 - 2014 GraphWalker
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import org.graphwalker.core.algorithm.Eulerian;
import org.graphwalker.core.condition.StopCondition;
import org.graphwalker.core.machine.Context;
import org.graphwalker.core.model.Element;
import org.graphwalker.core.model.Path;

import static org.graphwalker.core.model.Edge.RuntimeEdge;
import static org.graphwalker.core.model.Vertex.RuntimeVertex;

/**
 * @author Nils Olsson
 */
public final class ShortestAllPaths extends PathGeneratorBase<StopCondition> {

    private Path<Element> path = null;

    public ShortestAllPaths(StopCondition stopCondition) {
        setStopCondition(stopCondition);
    }

    @Override
    public Context getNextStep() {
        Context context = getContext();
        if (null == path) {
            path = getPath(context);
        }
        context.setCurrentElement(path.removeFirst());
        return context;
    }

    private Path<Element> getPath(Context context) {
        Element element = context.getCurrentElement();
        if (null == element) {
            element = context.getNextElement();
        }
        if (element instanceof RuntimeVertex) {
            return context.getAlgorithm(Eulerian.class).getEulerPath((RuntimeVertex)element);
        } else {
            return context.getAlgorithm(Eulerian.class).getEulerPath((RuntimeEdge)element);
        }
    }

    @Override
    public boolean hasNextStep() {
        return !getStopCondition().isFulfilled();
    }
}
