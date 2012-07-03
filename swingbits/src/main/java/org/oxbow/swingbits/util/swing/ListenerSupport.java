package org.oxbow.swingbits.util.swing;

import java.lang.reflect.Method;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

public class ListenerSupport<T> {

        private static final long serialVersionUID = 1L;

        private final Set<T> listeners = new HashSet<T>();

        public void add( T listener ) {
                if ( listener != null ) listeners.add(listener);
        }

        public void remove( T listener ) {
                if ( listener != null ) listeners.remove(listener);
        }

        public void clear() {
                listeners.clear();
        }

        private final Map<String, Method> methodCache = new HashMap<String, Method>();

        private <EVENT extends EventObject> void invokeMethod( final String methodName, final T listener, final EVENT event )  throws Exception {

                String key = methodName + ":"+ event.getClass().getName();

                Method method = methodCache.get(key);
                if ( method == null )  {
                        Class<? extends Object> cls = listener.getClass();
                        method = cls.getMethod(methodName, event.getClass());
                        method.setAccessible(true);
                        methodCache.put(key, method);
                }

                method.invoke(listener, event);

        }

        public <EVENT extends EventObject> void fire( final String methodName, final EVENT event ) throws Exception {

                Runnable r = new Runnable() {

                        @Override
                        public void run() {

                                try {
                                        for( T listener: listeners ) {
                                                invokeMethod(methodName, listener, event);
                                        }
                                } catch (Throwable ex) {
                                        throw new RuntimeException(ex);
                                }

                        }

                };

                if ( SwingUtilities.isEventDispatchThread()) {
                        r.run();
                } else {
                        SwingUtilities.invokeLater(r);
                }

        }


}