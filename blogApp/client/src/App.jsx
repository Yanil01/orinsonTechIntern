import "./App.css";
import axios from "axios";
import { useState, useEffect } from "react";

const API = axios.create({
  baseURL: "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

function App() {
  const [blogs, setBlogs] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editingBlogId, setEditingBlogId] = useState(null);

  const [errors, setErrors] = useState({
    userName: "",
    title: "",
    content: "",
  });

  const [newBlog, setNewBlog] = useState({
    userName: "",
    title: "",
    content: "",
  });

  // ---------------- FETCH ----------------
  const fetchBlogs = async () => {
    try {
      const res = await API.get("/blogs");
      setBlogs(res.data);
    } catch (err) {
      console.error("Fetch error:", err);
    }
  };

  useEffect(() => {
    fetchBlogs();
  }, []);

  // ---------------- VALIDATION ----------------
  const validateForm = () => {
    const newErrors = {
      userName: !newBlog.userName.trim() ? "Username is required" : "",
      title: !newBlog.title.trim() ? "Title is required" : "",
      content: !newBlog.content.trim() ? "Content is required" : "",
    };

    setErrors(newErrors);

    return !Object.values(newErrors).some(Boolean);
  };

  // ---------------- CREATE ----------------
  const submitBlog = async () => {
    if (!validateForm()) return;

    try {
      const res = await API.post("/blogs", newBlog);
      setBlogs((prev) => [...prev, res.data]);
      resetForm();
    } catch (err) {
      console.error("Create error:", err);
      alert("Failed to create blog.");
    }
  };

  // ---------------- DELETE ----------------
  const deleteBlog = async (id) => {
    try {
      await API.delete(`/blogs/${id}`);
      setBlogs((prev) => prev.filter((blog) => blog.id !== id));
    } catch (err) {
      console.error("Delete error:", err);
      alert("Failed to delete blog.");
    }
  };

  // ---------------- UPDATE ----------------
  const editBlog = async (id) => {
    if (!validateForm()) return;

    try {
      const res = await API.put(`/blogs/${id}`, newBlog);

      setBlogs((prev) =>
        prev.map((blog) => (blog.id === id ? res.data : blog)),
      );

      resetForm();
    } catch (err) {
      console.error("Update error:", err);
      alert("Failed to update blog.");
    }
  };

  // ---------------- RESET ----------------
  const resetForm = () => {
    setNewBlog({
      userName: "",
      title: "",
      content: "",
    });

    setErrors({
      userName: "",
      title: "",
      content: "",
    });

    setShowForm(false);
    setEditingBlogId(null);
  };

  // ---------------- EDIT FORM ----------------
  const openEditForm = (blog) => {
    setNewBlog({
      userName: blog.userName,
      title: blog.title,
      content: blog.content,
    });

    setErrors({
      userName: "",
      title: "",
      content: "",
    });

    setEditingBlogId(blog.id);
    setShowForm(true);
  };

  // ---------------- UI ----------------
  return (
    <div className="container">
      <header className="header">
        <h2>National Daily News</h2>

        <button
          className="btn primary"
          onClick={() => {
            resetForm();
            setShowForm(true);
          }}
        >
          Create Blog
        </button>
      </header>

      <section className="blog-list">
        <h3>Recent Blogs</h3>

        {blogs.length === 0 ? (
          <p>No blogs available.</p>
        ) : (
          blogs.map((blog) => (
            <div key={blog.id} className="blog-card">
              <h4>{blog.title}</h4>

              <p>{blog.content}</p>

              <div className="meta">
                <span>By: {blog.userName}</span>

                <span>
                  {blog.createdTime
                    ? new Date(blog.createdTime).toLocaleString()
                    : ""}
                </span>
              </div>

              <div className="actions">
                <button className="btn" onClick={() => openEditForm(blog)}>
                  Edit
                </button>

                <button
                  className="btn danger"
                  onClick={() => deleteBlog(blog.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))
        )}
      </section>

      {showForm && (
        <div className="modal">
          <div className="form">
            <h3>{editingBlogId ? "Edit Blog" : "Create Blog"}</h3>

            {/* Username */}
            <input
              type="text"
              placeholder="Username"
              value={newBlog.userName}
              onChange={(e) =>
                setNewBlog({
                  ...newBlog,
                  userName: e.target.value,
                })
              }
            />
            {errors.userName && <p className="error">{errors.userName}</p>}

            {/* Title */}
            <input
              type="text"
              placeholder="Title"
              value={newBlog.title}
              onChange={(e) =>
                setNewBlog({
                  ...newBlog,
                  title: e.target.value,
                })
              }
            />
            {errors.title && <p className="error">{errors.title}</p>}

            {/* Content */}
            <textarea
              placeholder="Content"
              rows="5"
              value={newBlog.content}
              onChange={(e) =>
                setNewBlog({
                  ...newBlog,
                  content: e.target.value,
                })
              }
            />
            {errors.content && <p className="error">{errors.content}</p>}

            <div className="form-actions">
              <button
                className="btn primary"
                onClick={() =>
                  editingBlogId ? editBlog(editingBlogId) : submitBlog()
                }
              >
                {editingBlogId ? "Update" : "Submit"}
              </button>

              <button className="btn" onClick={resetForm}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;
