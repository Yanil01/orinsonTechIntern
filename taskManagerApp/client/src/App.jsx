import axios from "axios";
import { useState, useEffect } from "react";
import "./App.css";

const API = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

function App() {
  const [tasks, setTasks] = useState([]);
  const [newTask, setNewTask] = useState({
    name: "",
    description: "",
    status: "pending",
  });
  const [editingTask, setEditingTask] = useState(null);

  const fetchTasks = async () => {
    try {
      const response = await API.get("/tasks");
      setTasks(response.data);
    } catch (error) {
      console.error("Error fetching tasks:", error);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!newTask.name || !newTask.descriptions) {
      alert("Please fill in all fields");
      return;
    }
    try {
      if (editingTask) {
        const response = await API.put(`/tasks/${editingTask}`, newTask);
        setTasks(
          tasks.map((task) => (task.id === editingTask ? response.data : task)),
        );
        setEditingTask(null);
      } else {
        const response = await API.post("/tasks", newTask);
        setTasks([...tasks, response.data]);
      }
      setNewTask({ name: "", descriptions: "", status: "pending" });
    } catch (error) {
      console.error("Error saving task:", error);
    }
  };

  const handleEdit = (task) => {
    setEditingTask(task.id);
    setNewTask({
      name: task.name,
      descriptions: task.descriptions,
      status: task.status,
    });
  };

  const cancelEdit = () => {
    setEditingTask(null);
    setNewTask({ name: "", descriptions: "", status: "pending" });
  };

  const handleDelete = async (id) => {
    console.log("Deleting task with ID:", id);
    if (!window.confirm("Are you sure you want to delete this task?")) return;
    try {
      await API.delete(`/tasks/${id}`);
      setTasks(tasks.filter((task) => task.id !== id));
    } catch (error) {
      console.error("Error deleting task:", error);
    }
  };

  return (
    <>
      <section id="center">
        <h1>To-Do List</h1>
        <p>Welcome to your To-Do List!</p>
        <p>
          Here you can add your tasks and keep track of what you need to do.
        </p>
        <form onSubmit={handleSubmit}>
          <table>
            <thead>
              <tr>
                <th>Task Name</th>
                <th>Description</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  <input
                    type="text"
                    name="name"
                    id="name"
                    placeholder="Enter your task name"
                    value={newTask.name}
                    onChange={(e) =>
                      setNewTask({ ...newTask, name: e.target.value })
                    }
                  />
                </td>
                <td>
                  <textarea
                    type="text"
                    name="description"
                    id="description"
                    placeholder="Enter your task description"
                    value={newTask.descriptions}
                    onChange={(e) =>
                      setNewTask({ ...newTask, descriptions: e.target.value })
                    }
                  />
                </td>
                <td>
                  <select
                    name="status"
                    id="status"
                    value={newTask.status}
                    onChange={(e) =>
                      setNewTask({ ...newTask, status: e.target.value })
                    }
                  >
                    <option value="pending">Pending</option>
                    <option value="in-progress">In Progress</option>
                    <option value="completed">Completed</option>
                  </select>
                </td>
                <td>
                  <button type="submit">
                    {editingTask ? "Update Task" : "Add Task"}
                  </button>
                  {editingTask && (
                    <button
                      type="button"
                      onClick={cancelEdit}
                      style={{ marginLeft: "5px" }}
                    >
                      Cancel
                    </button>
                  )}
                </td>
              </tr>
            </tbody>
          </table>
        </form>
        <div>
          <p>
            Your tasks will appear here once you add them. You can edit or
            delete tasks as needed to keep your list organized and up-to-date.
            Happy task managing!
          </p>
          <div>
            {/* This is where the list of tasks will be displayed */}
            <table>
              <thead>
                <tr>
                  <th>Task Name</th>
                  <th>Description</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {/* This is where the tasks will be rendered */}
                {tasks.map((task) => (
                  <tr key={task.id}>
                    <td>{task.name}</td>
                    <td>{task.descriptions}</td>
                    <td>{task.status}</td>
                    <td>
                      <button onClick={() => handleEdit(task)}>Edit</button>
                      <button onClick={() => handleDelete(task.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </>
  );
}

export default App;
