import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from "./App.tsx";

/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 * 
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 * 
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */

/**
 * This is the entrance of your React app. It renders your application
 * at the HTML element with id="root". Most often, you do not want to
 * change this file.
 */
const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
/**
 * Render your application, App is the component we define in App.tsx.
 */
root.render(
  
  <React.StrictMode>
    <App />
    
  </React.StrictMode>
  
);

