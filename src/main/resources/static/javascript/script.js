window.onload = function () {
    const loader = document.getElementById('loader-circle');
    loader.style.display = 'block';
    
    setTimeout(() => {
      loader.style.display = 'none';
    }, 1000); // adjust duration as needed
  };